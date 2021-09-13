package pl.lodz.uni.math.algorithms;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class DFS {

    private ArrayList<NodeModel> myNodes;
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private double [][] neighborTable;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;

    public DFS(ArrayList<NodeModel> myNodes, int startIndex, int endIndex, double[][] neighborTable, GraphPainter graphPainter) {
        this.myNodes = myNodes;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        visitedTable = new int [myNodes.size()];
        costTable = new BigDecimal [myNodes.size()];
        this.neighborTable = neighborTable;
        this.graphPainter = graphPainter;
    }
    
    public AlgorithmResult run(){
    	
    	long startTime = System.nanoTime();
    	
    	for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        costTable[startIndex] = BigDecimal.valueOf(0d);
        runDFS(startIndex, startIndex);
        visitedTable[startIndex] = -1;
        
        long endTime = System.nanoTime();
        
        graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
        return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);
    }
    
    public void runDFS(int actualNode, int parentNode){

        graphPainter.paintNodeAsActualNode(myNodes.get(actualNode).getName());
    	
    	visitedTable[actualNode] = parentNode;
    	
    	for(int i = 0; i < myNodes.size(); ++i){
    		if(neighborTable[actualNode][i] > 0  && visitedTable[i] == -1){
    			costTable[i] = costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][i]));
                graphPainter.paintEdge(myNodes.get(actualNode).getName(), myNodes.get(i).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
    			graphPainter.paintNode(myNodes.get(i).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
    	        graphPainter.paintNode(myNodes.get(actualNode).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
    	        runDFS(i, actualNode);
    	        graphPainter.paintNodeAsActualNode(myNodes.get(actualNode).getName());
    		}
    	}
    	graphPainter.paintNode(myNodes.get(actualNode).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
    }
    
}
