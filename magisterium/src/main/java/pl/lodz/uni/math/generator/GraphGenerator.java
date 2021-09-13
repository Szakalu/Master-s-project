package pl.lodz.uni.math.generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.NodeModel;

public class GraphGenerator {
	
	private final static int FIRST_CREATE_CHAIN = 1;
	private final static int FIRST_CREATE_RANDOM = 2;
	
	private int targetNodesNumber;
	private int targetEdgesNumber;
	private String filePath;
	private int firstCreate;
	
	
	public GraphGenerator(int targetNodesNumber, int targetEdgesNumber, String filePath, int firstCreate) {
		super();
		this.targetNodesNumber = targetNodesNumber;
		this.targetEdgesNumber = targetEdgesNumber;
		this.filePath = filePath;
		this.firstCreate = firstCreate;
	}



	public double [][] generateGraph(int minWeight, int maxWeight){
		
		int maxNumberOfEdges = (targetNodesNumber *(targetNodesNumber-1))/2;
		
		ArrayList<Integer> nodesList = new ArrayList<Integer>();
		double [][] neighborsMatrix = new double [targetNodesNumber][targetNodesNumber];
		
		
		if(targetEdgesNumber == maxNumberOfEdges){
			generateIntegerGraphForMaxEdges(neighborsMatrix, nodesList, minWeight, maxWeight);
		}
		else if(targetEdgesNumber < maxNumberOfEdges){
			
			ArrayList<EdgeModel> consistentEdges = new ArrayList<EdgeModel>();
			
			if(firstCreate == FIRST_CREATE_RANDOM){
				ArrayList<Integer> tempNodesList = new ArrayList<Integer>();
				
				for(int nodeIndex = 0; nodeIndex < targetNodesNumber; nodeIndex++){
					tempNodesList.add(nodeIndex);
				}
				
				int firstNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
				nodesList.add(tempNodesList.get(firstNodeIndex));
				EdgeModel edge = new EdgeModel(tempNodesList.get(firstNodeIndex), -1);
				tempNodesList.remove(firstNodeIndex);
				
				int secondNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
				nodesList.add(tempNodesList.get(secondNodeIndex));
				edge.setEndIndex(tempNodesList.get(secondNodeIndex));
				tempNodesList.remove(secondNodeIndex);
				
				consistentEdges.add(edge);
				neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
				neighborsMatrix[edge.getEndIndex()][edge.getStartIndex()] = neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()];
				
				
				while(tempNodesList.size() > 0){
					firstNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
					secondNodeIndex = ThreadLocalRandom.current().nextInt(0, nodesList.size());
					
					consistentEdges.add(new EdgeModel(tempNodesList.get(firstNodeIndex), nodesList.get(secondNodeIndex)));
					neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
					neighborsMatrix[nodesList.get(secondNodeIndex)][tempNodesList.get(firstNodeIndex)] = neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)];
					
					nodesList.add(tempNodesList.get(firstNodeIndex));
					tempNodesList.remove(firstNodeIndex);
					
					if(tempNodesList.size() == 1){
						firstNodeIndex = 0;
						secondNodeIndex = ThreadLocalRandom.current().nextInt(0, nodesList.size());
						
						consistentEdges.add(new EdgeModel(tempNodesList.get(firstNodeIndex), nodesList.get(secondNodeIndex)));
						neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
						neighborsMatrix[nodesList.get(secondNodeIndex)][tempNodesList.get(firstNodeIndex)] = neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)];
						
						nodesList.add(tempNodesList.get(firstNodeIndex));
						tempNodesList.remove(firstNodeIndex);
					}
				}
				
			}
			else if(firstCreate == FIRST_CREATE_CHAIN){
				
				nodesList.add(0);
				
				for(int nodeIndex = 1; nodeIndex < targetNodesNumber; nodeIndex++){
					EdgeModel edge = new EdgeModel(nodeIndex-1, nodeIndex);
					consistentEdges.add(edge);
					neighborsMatrix[nodeIndex-1][nodeIndex] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
					neighborsMatrix[nodeIndex][nodeIndex-1] = neighborsMatrix[nodeIndex-1][nodeIndex];
					nodesList.add(nodeIndex);
				}
			} 
			
			int row;
			int column;
			int actualEdgesNumber = consistentEdges.size();
			
			if(targetEdgesNumber <= (float)maxNumberOfEdges/2){
				actualEdgesNumber = consistentEdges.size();
				
				while(actualEdgesNumber < targetEdgesNumber){
					row = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					column = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					if(row != column && neighborsMatrix[row][column] == 0){
						neighborsMatrix[row][column] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
						neighborsMatrix[column][row] = neighborsMatrix[row][column];
						actualEdgesNumber++;
					}
				}
			}
			else if(targetEdgesNumber > (float)maxNumberOfEdges/2){
				for(int i = 0; i < targetNodesNumber; i++){
					int j = i;
					for(j = 0; j < targetNodesNumber; j++){
						if(i != j && neighborsMatrix[i][j] == 0){
							neighborsMatrix[i][j] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
							neighborsMatrix[j][i] = neighborsMatrix[i][j];
						}
					}
				}
				
				actualEdgesNumber = maxNumberOfEdges;
				
				while(actualEdgesNumber > targetEdgesNumber){
					row = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					column = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					
					if(row != column && neighborsMatrix[row][column] > 0){
						EdgeModel edgeCandidate = new EdgeModel(row, column);
						if(!checkIfEdgeIsConsistent(consistentEdges, edgeCandidate)){
							neighborsMatrix[row][column] = 0;
							neighborsMatrix[column][row] = 0;
							actualEdgesNumber--;
						}
					}
				}
			}
		}
		int counter = 0;
		for(int i = 0; i < targetNodesNumber; i++){
			for(int j = 0; j < targetNodesNumber; j++){
				if(neighborsMatrix[i][j] != 0){
					counter++;
				}
			}
		}
		System.out.println(counter);
		saveToFile(nodesList, neighborsMatrix);
		return neighborsMatrix;
	}
	
	private void generateIntegerGraphForMaxEdges(double [][] neighborsMatrix, ArrayList<Integer> nodesList, int minWeight, int maxWeight){
		for(int i = 0; i < targetNodesNumber; i++){
			nodesList.add(i);
			for(int j = i; j < targetNodesNumber; j++){
				if(i == j){
					neighborsMatrix[i][j] = 0;
				}
				else{
					neighborsMatrix[i][j] = ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
					neighborsMatrix[j][i] = neighborsMatrix[i][j];
				}
			}
		}
	}
	
	public double [][] generateGraph(double minWeight, double maxWeight){
		
		int maxNumberOfEdges = (targetNodesNumber *(targetNodesNumber-1))/2;
		
		ArrayList<Integer> nodesList = new ArrayList<Integer>();
		double [][] neighborsMatrix = new double [targetNodesNumber][targetNodesNumber];
		
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	    decimalFormatSymbols.setDecimalSeparator('.');
	    decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	    
		
		if(targetEdgesNumber == maxNumberOfEdges){
			generateDoubleGraphForMaxEdges(neighborsMatrix, nodesList, minWeight, maxWeight, decimalFormat);
		}
		else if(targetEdgesNumber < maxNumberOfEdges){
			

			ArrayList<EdgeModel> consistentEdges = new ArrayList<EdgeModel>();
			
			if(firstCreate == FIRST_CREATE_RANDOM){
				ArrayList<Integer> tempNodesList = new ArrayList<Integer>();
				
				for(int nodeIndex = 0; nodeIndex < targetNodesNumber; nodeIndex++){
					tempNodesList.add(nodeIndex);
				}
				
				int firstNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
				nodesList.add(tempNodesList.get(firstNodeIndex));
				EdgeModel edge = new EdgeModel(tempNodesList.get(firstNodeIndex), -1);
				tempNodesList.remove(firstNodeIndex);
				
				int secondNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
				nodesList.add(tempNodesList.get(secondNodeIndex));
				edge.setEndIndex(tempNodesList.get(secondNodeIndex));
				tempNodesList.remove(secondNodeIndex);
				
				consistentEdges.add(edge);
				neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
				neighborsMatrix[edge.getEndIndex()][edge.getStartIndex()] = neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()];
				
				
				while(tempNodesList.size() > 0){
					firstNodeIndex = ThreadLocalRandom.current().nextInt(0, tempNodesList.size());
					secondNodeIndex = ThreadLocalRandom.current().nextInt(0, nodesList.size());
					
					consistentEdges.add(new EdgeModel(tempNodesList.get(firstNodeIndex), nodesList.get(secondNodeIndex)));
					neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
					neighborsMatrix[nodesList.get(secondNodeIndex)][tempNodesList.get(firstNodeIndex)] = neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)];
					
					nodesList.add(tempNodesList.get(firstNodeIndex));
					tempNodesList.remove(firstNodeIndex);
					
					if(tempNodesList.size() == 1){
						firstNodeIndex = 0;
						secondNodeIndex = ThreadLocalRandom.current().nextInt(0, nodesList.size());
						
						consistentEdges.add(new EdgeModel(tempNodesList.get(firstNodeIndex), nodesList.get(secondNodeIndex)));
						neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
						neighborsMatrix[nodesList.get(secondNodeIndex)][tempNodesList.get(firstNodeIndex)] = neighborsMatrix[tempNodesList.get(firstNodeIndex)][nodesList.get(secondNodeIndex)];
						
						nodesList.add(tempNodesList.get(firstNodeIndex));
						tempNodesList.remove(firstNodeIndex);
					}
				}
				
			}
			else if(firstCreate == FIRST_CREATE_CHAIN){
				
				nodesList.add(0);
				
				for(int nodeIndex = 1; nodeIndex < targetNodesNumber; nodeIndex++){
					EdgeModel edge = new EdgeModel(nodeIndex-1, nodeIndex);
					consistentEdges.add(edge);
					neighborsMatrix[nodeIndex-1][nodeIndex] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
					neighborsMatrix[nodeIndex][nodeIndex-1] = neighborsMatrix[nodeIndex-1][nodeIndex];
					nodesList.add(nodeIndex);
				}
			}
			
			int row;
			int column;
			int actualEdgesNumber = consistentEdges.size();
			if(targetEdgesNumber <= (float)maxNumberOfEdges/2){
				actualEdgesNumber = consistentEdges.size();
				
				while(actualEdgesNumber < targetEdgesNumber){
					row = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					column = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					if(row != column && neighborsMatrix[row][column] == 0){
						neighborsMatrix[row][column] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
						neighborsMatrix[column][row] = neighborsMatrix[row][column];
						actualEdgesNumber++;
					}
				}
			}
			else if(targetEdgesNumber > (float)maxNumberOfEdges/2){
				for(int i = 0; i < targetNodesNumber; i++){
					for(int j = i; j < targetNodesNumber; j++){
						if(i != j && neighborsMatrix[i][j] == 0){
							neighborsMatrix[i][j] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
							neighborsMatrix[j][i] = neighborsMatrix[i][j];
						}
					}
				}
				
				actualEdgesNumber = maxNumberOfEdges;
				
				while(actualEdgesNumber > targetEdgesNumber){
					row = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					column = ThreadLocalRandom.current().nextInt(0, targetNodesNumber);
					
					if(row != column && neighborsMatrix[row][column] > 0){
						EdgeModel edgeCandidate = new EdgeModel(row, column);
						if(!checkIfEdgeIsConsistent(consistentEdges, edgeCandidate)){
							neighborsMatrix[row][column] = 0;
							neighborsMatrix[column][row] = 0;
							actualEdgesNumber--;
						}
					}
				}
			}
		}
		int counter = 0;
		for(int i = 0; i < targetNodesNumber; i++){
			for(int j = 0; j < targetNodesNumber; j++){
				if(neighborsMatrix[i][j] != 0){
					counter++;
				}
			}
		}
		System.out.println(counter);
		saveToFile(nodesList, neighborsMatrix);
		return neighborsMatrix;
	}
	
	private boolean checkIfEdgeIsConsistent(ArrayList<EdgeModel> consistentEdges, EdgeModel edgeCandidate){
		for(EdgeModel edge: consistentEdges){
			if(edge.getStartIndex() == edgeCandidate.getStartIndex() && edge.getEndIndex() == edgeCandidate.getEndIndex()){
				return true;
			}
			else if(edge.getStartIndex() == edgeCandidate.getEndIndex() && edge.getEndIndex() == edgeCandidate.getStartIndex()){
				return true;
			}
		}
		return false;
	}

	private void generateDoubleGraphForMaxEdges(double [][] neighborsMatrix, ArrayList<Integer> nodesList, double minWeight, double maxWeight, DecimalFormat decimalFormat){
		for(int i = 0; i < targetNodesNumber; i++){
			nodesList.add(i);
			for(int j = i; j < targetNodesNumber; j++){
				if(i == j){
					neighborsMatrix[i][j] = 0d;
				}
				else{
					neighborsMatrix[i][j] = Double.parseDouble(decimalFormat.format(ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)));
					neighborsMatrix[j][i] = neighborsMatrix[i][j];
				}
			}
		}
	}
	
	private void saveToFile(ArrayList<Integer> nodesList, double [][] neighborsMatrix){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
			writer.println("NODE");
			
			for(int i = 0; i < nodesList.size(); ++i){
				writer.println(i + " " + 0);
			}
			writer.println("EDGE");
			for(int i = 0; i < nodesList.size(); ++i){
				for(int j = i; j < nodesList.size(); ++j){
					if(i!=j && neighborsMatrix[i][j] > 0){
						writer.println(i + " " + j + " " + neighborsMatrix[i][j]);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			writer.close();
		}
	}
}
