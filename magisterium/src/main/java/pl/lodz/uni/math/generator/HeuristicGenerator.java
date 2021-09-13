package pl.lodz.uni.math.generator;

import java.math.BigDecimal;
import java.util.ArrayList;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.model.UndirectedGraphModel;
import pl.lodz.uni.math.text.file.TextFileManager;

public class HeuristicGenerator {
	

	public HeuristicGenerator() {
		
	}
	
	public void generateHeuristicForGraphFromFile(String graphPath, String endNode){
		UndirectedGraphModel graphModel = new UndirectedGraphModel();
		TextFileManager.readGraphFromFile(graphModel, graphPath);
		
		ArrayList<Integer> nodesToCheck = extractNodeIndexesFromNodesList(graphModel.getNodesList());
		for(int nodeIndex = 0; nodeIndex < nodesToCheck.size(); ++nodeIndex){
			graphModel.getNodesList().get(nodeIndex).setHeuristicValue(dijkstraRun(nodesToCheck, graphModel.getNeighborsMatrix(), nodeIndex, graphModel.getNodeIndexNumber(endNode)));
		}
		
		TextFileManager.saveGraphToFile(graphPath, graphModel);
	}
	
	public static void generateHeuristicForVisualizedGraph(UndirectedGraphModel graphModel){
		ArrayList<Integer> nodesToCheck = extractNodeIndexesFromNodesList(graphModel.getNodesList());
		
		for(int nodeIndex = 0; nodeIndex < nodesToCheck.size(); ++nodeIndex){
			graphModel.getNodesList().get(nodeIndex).setHeuristicValue(dijkstraRun(nodesToCheck, graphModel.getNeighborsMatrix(), nodeIndex, graphModel.getNodeIndexNumber(graphModel.getStopNode())));
		}
	}
	
	private static double dijkstraRun(ArrayList<Integer> nodesToCheckk, double [][] neighborsMatrix, int startIndex, int endIndex){
    	
    	ArrayList<Integer> nodesToCheck = new ArrayList<Integer>(nodesToCheckk);
    	
    	int visitedTable[] = new int [nodesToCheck.size()];
    	BigDecimal costTable [] = new BigDecimal [nodesToCheck.size()];
    	
        for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        
        costTable[startIndex] = BigDecimal.valueOf(0d);
        visitedTable[startIndex] = startIndex;
        
        BigDecimal lowestCost;
        int actualNode;
        
        while(nodesToCheck.size() != 0){
        	
        	int index = 0;
        	int actualNodeIndex = index;
            lowestCost = costTable[nodesToCheck.get(0)];
            actualNode = nodesToCheck.get(0);
            
            for(int node: nodesToCheck){
                if(costTable[node].compareTo(lowestCost) < 0){
                    lowestCost = costTable[node];
                    actualNode = node;
                    actualNodeIndex=index;
                }
                index++;
            }
            
            if(actualNode == endIndex){
            	return costTable[actualNode].doubleValue();
            }
            
            nodesToCheck.remove(actualNodeIndex);
            
            
            for(int i = 0; i < costTable.length; i++){
                if(neighborsMatrix[actualNode][i] == 0){
                    continue;
                }
                if(costTable[i].compareTo(costTable[actualNode].add(BigDecimal.valueOf(neighborsMatrix[actualNode][i]))) > 0){
                	costTable[i] = costTable[actualNode].add(BigDecimal.valueOf(neighborsMatrix[actualNode][i]));
                    visitedTable[i] = actualNode;
                }
            }
        }
        
        return Double.MAX_VALUE;
    }

	private static ArrayList<Integer> extractNodeIndexesFromNodesList(ArrayList<NodeModel> nodesList){
		ArrayList<Integer> extractedNodesIndexes = new ArrayList<Integer>();
		for(NodeModel node: nodesList){
			extractedNodesIndexes.add(node.getNumber());
		}
		return extractedNodesIndexes;
	}

}
