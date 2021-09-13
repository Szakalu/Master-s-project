package pl.lodz.uni.math.algorithms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

public class AntSystemCycle {
	
	private ArrayList<NodeModel> nodesList;
	private double [][] neighborsMatrix;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;
    private double alfa;
    private double beta;
    private double initialPheromones;
    private int numberOfAnts;
    private double pheromonesPlus;
    private double pheromonesMinus;
    private int numberOfIterations;

	//TODO dorobic przerwy miedzy iteracjami
    public AntSystemCycle(ArrayList<NodeModel> nodesList, double[][] neighborsMatrix, int startIndex, int endIndex,
    		GraphPainter graphPainter, double alfa, double beta, double initialPheromones, int numberOfAnts, 
    		double pheromonesPlus, double pheromonesMinus, int numberOfIterations) {
    	this.nodesList = nodesList;
		this.neighborsMatrix = neighborsMatrix;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.alfa = alfa;
		this.beta = beta;
		this.graphPainter = graphPainter;
		this.initialPheromones = initialPheromones;
		this.numberOfAnts = numberOfAnts;
		this.pheromonesPlus = pheromonesPlus;
		this.pheromonesMinus = pheromonesMinus;
		this.numberOfIterations = numberOfIterations;
	}
    
    public void run(){
    	
    	double [][] pheromonesMatrix = new double [nodesList.size()][nodesList.size()];
    	double [][] visibilityMatrix = new double [nodesList.size()][nodesList.size()];
    	
    	for(int row = 0; row < pheromonesMatrix.length; ++row){
    		for(int column = row; column < pheromonesMatrix.length; ++column){
    			if(neighborsMatrix[row][column] > 0){
    				pheromonesMatrix[row][column] = initialPheromones;
    				pheromonesMatrix[column][row] = initialPheromones;
    				visibilityMatrix[row][column] = 1.0d/neighborsMatrix[row][column];
    				visibilityMatrix[column][row] = visibilityMatrix[row][column];
    			}
    			else{
    				pheromonesMatrix[row][column] = 0;
    				pheromonesMatrix[column][row] = 0;
    				visibilityMatrix[row][column] = 0;
    				visibilityMatrix[column][row] = 0;
    			}
    		}
    	}
    	paintAllEdgesWithActualPheromones(pheromonesMatrix);
    	
    	double bestRoadLenght = Double.MAX_VALUE;
    	ArrayList<Integer> bestRoad = new ArrayList<Integer>();
    	
    	ArrayList<Ant> ants = new ArrayList<Ant>();
    	
    	for(int antNumber = 0; antNumber < numberOfAnts; antNumber++){
    		ants.add(new Ant(antNumber));
    	}
    	
    	//iteracja systemu
    	int iterationCounter = 0;
    	while(iterationCounter < numberOfIterations){
    		//resetowanie mrówek
    		for(Ant ant: ants){
    			ant.clearTabuList();
    			ant.clearWithdawnList();
    			ant.setFoundEnd(false);
    			ant.getTabuList().add(startIndex);
    		}
    		//ustawienie tablicy visibilityMultPheromones zawierajacej pomnozony feromon i atrakcyjnosc wierzchołka
    		double [][] visibilityMultPheromones = new double [nodesList.size()][nodesList.size()];
    		calculateVisibilityMultPheromonesMatrix(visibilityMatrix, pheromonesMatrix, visibilityMultPheromones);
    		
    		//ruch mrowek
    		boolean allAntsFoundEnd = false;
    		while(!allAntsFoundEnd){
    			allAntsFoundEnd = true;
    			for(Ant ant: ants){
    				if(ant.isFoundEnd()){
    					continue;
    				}
    				//wybor nastepnego wierzcholka dla danej mrowki
    				for(int nodeIndex = 0; nodeIndex < visibilityMultPheromones.length; nodeIndex++){
    					if(visibilityMultPheromones[ant.getTabuList().get(ant.getTabuList().size()-1)][nodeIndex] > 0){
    						if(ant.getTabuList().contains(nodeIndex) || ant.getWithdrawnList().contains(nodeIndex)){
    							continue;
    						}
    						ant.addNewNodeCandidate(nodeIndex, visibilityMultPheromones[ant.getTabuList().get(ant.getTabuList().size()-1)][nodeIndex]);
    						allAntsFoundEnd=false;
    					}
    				}
    				//cofanie mrowki w razie braku kolejnych mozliwosci drog
    				if(ant.getProbablisticNodesList().size() == 0){
    					ant.getWithdrawnList().add(ant.getTabuList().get(ant.getTabuList().size()-1));
    					ant.getTabuList().remove(ant.getTabuList().size()-1);
    					allAntsFoundEnd=false;
    				}
    				ant.calculateProbablistics();
    				ant.calculateProbablisticForDraw();
    				int drawnNode = ant.drawNextNode(ThreadLocalRandom.current().nextDouble(0, 1), nodesList);

    				ant.clearProbablisticNodeList();
    				//wybrany wierzcholek jest ostatnim, mrowka znalazla koniec
    				if(drawnNode == endIndex){
    					ant.setFoundEnd(true);
    				}
    			}
    			
    		}
    		iterationCounter++;
    		System.out.println(iterationCounter);
    		updatePheromonsMinus(pheromonesMatrix);
    		for(Ant ant: ants){
    			double antRoadLength = calculateAntRoadLength(ant.getTabuList());
    			if(bestRoadLenght > antRoadLength){
    				bestRoadLenght = antRoadLength;
    				bestRoad = new ArrayList<Integer>(ant.getTabuList());
    			}
    			updatePheromonsPlus(ant.getTabuList(), pheromonesMatrix, pheromonesPlus/antRoadLength);
    		}
    		paintAllEdgesWithActualPheromones(pheromonesMatrix);
    		//elitest update
//    		for(int i = 0; i < 5; ++i){
//    			updatePheromonsPlus(bestRoad, pheromonesMatrix, pheromonesPlus/bestRoadLenght);
//    		}
    	}
    	paintRoadChosenByAnts(pheromonesMatrix);
    	System.out.println(bestRoadLenght);
    }
    
    private void calculateVisibilityMultPheromonesMatrix(double [][] visibilityMatrix, double [][] pheromonesMatrix, double [][] visibilityMultPheromones){
		for(int row = 0; row < visibilityMatrix.length; ++row){
			for(int column = row; column < visibilityMatrix.length; ++column){
				if(visibilityMatrix[row][column] > 0){
					if(pheromonesMatrix[row][column] > 0){
						visibilityMultPheromones[row][column] = BigDecimal.valueOf(Math.pow(visibilityMatrix[row][column], beta)).multiply(BigDecimal.valueOf(Math.pow(pheromonesMatrix[row][column], alfa))).doubleValue();
					}
					else{
						visibilityMultPheromones[row][column] = BigDecimal.valueOf(Math.pow(visibilityMatrix[row][column], beta)).doubleValue();
    					
					}
					visibilityMultPheromones[column][row] = visibilityMultPheromones[row][column];
				}
			}
		}
	}
    
    private void paintRoadChosenByAnts(double [][] pheromonesMatrix){
    	ArrayList<Integer> antsRoad = new ArrayList<Integer>();
    	int actualNodeNumber = startIndex;
    	graphPainter.paintNodeWithoutStop(nodesList.get(actualNodeNumber).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
    	antsRoad.add(actualNodeNumber);
    	while(actualNodeNumber != endIndex){
    		int nextNodeNumber = getNodeNumberWithBiggestPheromones(pheromonesMatrix, actualNodeNumber, antsRoad);
    		graphPainter.paintEdge(nodesList.get(actualNodeNumber).getName(), nodesList.get(nextNodeNumber).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
            actualNodeNumber = nextNodeNumber;
    		graphPainter.paintNodeWithoutStop(nodesList.get(actualNodeNumber).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
    		antsRoad.add(actualNodeNumber);
    	}
    }
    
    private int getNodeNumberWithBiggestPheromones(double [][] pheromonesMatrix, int rowNodeNumber, ArrayList<Integer> antsRoad){
    	double biggestPheromones = Double.MIN_VALUE;
    	int bestNodeNumber = 0;
    	for(int nodeNumber = 0; nodeNumber < pheromonesMatrix.length; nodeNumber++){
    		if(pheromonesMatrix[rowNodeNumber][nodeNumber] > 0 && pheromonesMatrix[rowNodeNumber][nodeNumber] > biggestPheromones && !antsRoad.contains(nodeNumber)){
    			bestNodeNumber = nodeNumber;
    			biggestPheromones = pheromonesMatrix[rowNodeNumber][nodeNumber];
    		}
    	}
    	return bestNodeNumber;
    }
    
    private void updatePheromonsMinus(double [][] pheromonesMatrix){
    	for(int row = 0; row < pheromonesMatrix.length; ++row){
    		for(int column = row; column < pheromonesMatrix.length; ++column){
    			if(neighborsMatrix[row][column] > 0){
    				pheromonesMatrix[row][column] = pheromonesMatrix[row][column] * (1-pheromonesMinus);
    				pheromonesMatrix[column][row] = pheromonesMatrix[row][column];
    			}
    		}
    	}
    }
    
    private void updatePheromonsPlus(ArrayList<Integer> tabuList, double [][] pheromonesMatrix, double pheromonsValuePlus){
    	for(int index = 1; index < tabuList.size(); index++){
    		pheromonesMatrix[tabuList.get(index-1)][tabuList.get(index)] = BigDecimal.valueOf(pheromonesMatrix[tabuList.get(index-1)][tabuList.get(index)]).
    				add(BigDecimal.valueOf(pheromonsValuePlus)).doubleValue();
    		pheromonesMatrix[tabuList.get(index)][tabuList.get(index-1)] = pheromonesMatrix[tabuList.get(index-1)][tabuList.get(index)];
    	}
    }
    
    private double calculateAntRoadLength(ArrayList<Integer> tabuList){
    	double sum = 0;
    	for(int index = 1; index < tabuList.size(); index++){
    		sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(neighborsMatrix[tabuList.get(index-1)][tabuList.get(index)])).doubleValue();
    	}
    	return sum;
    }
    
    private void printAntTabuList(ArrayList<Integer> tabuList){
    	System.out.println();
    	for(int index = 0; index < tabuList.size(); index++){
    		System.out.print(nodesList.get(tabuList.get(index)).getName() + " -> ");
    	}
    	System.out.println();
    }
    
    private void paintAllEdgesWithActualPheromones(double [][] pheromonesMatrix){
    	for(int row = 0; row < pheromonesMatrix.length; ++row){
    		for(int column = row; column < pheromonesMatrix.length; ++column){
    			if(neighborsMatrix[row][column] > 0){
    				graphPainter.paintEdgeWithActualPheromone(nodesList.get(row).getName(), nodesList.get(column).getName(), pheromonesMatrix[row][column]);
    			}
    		}
    	}
    }

}
