package pl.lodz.uni.math.algorithms;

import java.math.BigDecimal;
import java.util.ArrayList;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

public class AStar {
	
	private ArrayList<NodeModel> nodesList;
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private BigDecimal [] fFunctionTable;
    private double [][] neighborTable;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;
	
	public AStar(ArrayList<NodeModel> nodesList, int startIndex, int endIndex, double[][] neighborTable, GraphPainter graphPainter){
		this.nodesList = nodesList;
        this.neighborTable = neighborTable;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.visitedTable = new int [nodesList.size()];
        this.costTable = new BigDecimal [nodesList.size()];
        this.fFunctionTable = new BigDecimal [nodesList.size()];
        this.graphPainter = graphPainter;
	}
	
	public AlgorithmResult run(){
		
		long startTime = System.nanoTime();
		
		for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
            fFunctionTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
		
        fFunctionTable[startIndex] = BigDecimal.valueOf(0d); 
		costTable[startIndex] = BigDecimal.valueOf(0d);
        visitedTable[startIndex] = startIndex;
        
        ArrayList<Integer> openList = new ArrayList<Integer>();
		ArrayList<Integer> closedList = new ArrayList<Integer>();
		
		openList.add(startIndex);
		int actualNode;
		int lowestFFunctionNodeIndex;
		boolean updateNode = false;
		while(openList.size() > 0){
			lowestFFunctionNodeIndex = getNodeIndexWithLowestFFunction(openList);
			actualNode = openList.get(lowestFFunctionNodeIndex);
			openList.remove(lowestFFunctionNodeIndex);
			if(actualNode == endIndex){
				break;
			}
			closedList.add(actualNode);
			
            graphPainter.paintNodeAsActualNode(nodesList.get(actualNode).getName());
			
			for(int nodeIndex = 0; nodeIndex < nodesList.size(); nodeIndex++){
				if(neighborTable[actualNode][nodeIndex] > 0){
					double fFunction = costTable[actualNode].add(BigDecimal.valueOf(nodesList.get(nodeIndex).getHeuristicValue())).doubleValue();
					fFunction = BigDecimal.valueOf(fFunction).add(BigDecimal.valueOf(neighborTable[actualNode][nodeIndex])).doubleValue();
					System.out.println("From: " + nodesList.get(actualNode).getName() + " to: " + nodesList.get(nodeIndex).getName() + " " + fFunction);
					updateNode = false;
					if(closedList.contains(nodeIndex)){
						if(costTable[nodeIndex].compareTo(costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][nodeIndex]))) > 0){
							openList.add(nodeIndex);
							removeNodeFromeClosedList(nodeIndex, closedList);
							updateNode = true;
						}
					}
					else if(!openList.contains(nodeIndex)){
						openList.add(nodeIndex);
						updateNode = true;
					}
					else if(costTable[nodeIndex].compareTo(costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][nodeIndex]))) > 0){
						updateNode = true;
					}
					if(updateNode){
						if(visitedTable[nodeIndex] != -1){
	                		graphPainter.paintEdge(nodesList.get(nodeIndex).getName(), nodesList.get(visitedTable[nodeIndex]).getName(), Settings.STYL_NORMALNEJ_KRAWEDZI);
	                	}
						
						visitedTable[nodeIndex] = actualNode;
						costTable[nodeIndex] = costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][nodeIndex]));
						fFunctionTable[nodeIndex] = costTable[nodeIndex].add(BigDecimal.valueOf(nodesList.get(nodeIndex).getHeuristicValue()));
						
						graphPainter.paintEdge(nodesList.get(actualNode).getName(), nodesList.get(nodeIndex).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
	                	graphPainter.paintNode(nodesList.get(nodeIndex).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
					}
				}
			}
			graphPainter.paintNode(nodesList.get(actualNode).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
		}
		
		visitedTable[startIndex] = -1;
		
		graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
        long endTime = System.nanoTime();
        
        return new AlgorithmResult(costTable, visitedTable, endTime-startTime, startIndex, endIndex);
        
	}
	
	private int getNodeIndexWithLowestFFunction(ArrayList<Integer> openList){
		BigDecimal lowestFFunction = fFunctionTable[openList.get(0)];
		int lowestFFunctionNodeIndex = 0;
		for(int i = 0; i < openList.size(); ++i){
			if(fFunctionTable[openList.get(i)].compareTo(lowestFFunction) < 0){
				lowestFFunction = fFunctionTable[openList.get(i)];
				lowestFFunctionNodeIndex = i;
			}
		}
		return lowestFFunctionNodeIndex;
	}
	
	private void removeNodeFromeClosedList(int nodeIndexToRemove, ArrayList<Integer> closedList){
		for(int index = 0; index < closedList.size(); index++){
			if(closedList.get(index) == nodeIndexToRemove){
				closedList.remove(index);
				break;
			}
		}
	}
		

}
