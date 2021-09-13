package pl.lodz.uni.math.algorithms;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BellmanFord {

    private ArrayList<NodeModel> myNodes;
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private double [][] neighborTable;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;

    public BellmanFord(ArrayList<NodeModel> myNodes, int startIndex, int endIndex, double[][] neighborTable, GraphPainter graphPainter) {
        this.myNodes = myNodes;
        this.neighborTable = neighborTable;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.visitedTable = new int [myNodes.size()];
        this.costTable = new BigDecimal [myNodes.size()];
        this.graphPainter = graphPainter;
    }

    public AlgorithmResult run(){
    	
    	long startTime = System.nanoTime();

        for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        visitedTable[startIndex] = startIndex;
        costTable[startIndex] = BigDecimal.valueOf(0d);
        
        boolean graphUpdated = true; 
        
        for(int i = 1; i < myNodes.size(); ++i){
        	if(!graphUpdated){
        		break;
        	}
        	graphUpdated = false;
        	for(int row = 0; row < myNodes.size(); ++row){
        		graphPainter.paintNodeAsActualNode(myNodes.get(row).getName());
        		for(int column = 0; column < myNodes.size(); ++column){
        			if(neighborTable[row][column] > 0 && costTable[row].compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) < 0){
                		if(costTable[column].compareTo(costTable[row].add(BigDecimal.valueOf(neighborTable[row][column]))) > 0){
                			graphUpdated = true;
                			if(visitedTable[column] != -1){
                        		graphPainter.paintEdge(myNodes.get(column).getName(), myNodes.get(visitedTable[column]).getName(), Settings.STYL_NORMALNEJ_KRAWEDZI);
                        	}
                			graphPainter.paintEdge(myNodes.get(row).getName(), myNodes.get(column).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
                        	graphPainter.paintNode(myNodes.get(column).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
                			costTable[column] = costTable[row].add(BigDecimal.valueOf(neighborTable[row][column]));
                            visitedTable[column] = row;
                		}
                	}
        		}
            	graphPainter.paintNode(myNodes.get(row).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
        	}
        }
        
        
//        for(int i = 1; i < myNodes.size(); ++i){
//            for(EdgeModel myEdge : doubleEdgesList){
//            	if(costTable[myEdge.getStartIndex()].compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) != 0){
//            		if(costTable[myEdge.getEndIndex()].compareTo(costTable[myEdge.getStartIndex()].add(BigDecimal.valueOf(myEdge.getWeight()))) > 0){
//            			costTable[myEdge.getEndIndex()] = costTable[myEdge.getStartIndex()].add(BigDecimal.valueOf(myEdge.getWeight()));
//                        visitedTable[myEdge.getEndIndex()] = myEdge.getStartIndex();
//            		}
//            	}
//            }
//        }
        visitedTable[startIndex] = -1;

        long endTime = System.nanoTime();
        
        graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
        return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);
    }

}
