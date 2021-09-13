package pl.lodz.uni.math.algorithms;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    private ArrayList<NodeModel> nodesList;
    private Queue<Integer> queue = new LinkedList<Integer>();
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private double [][] neighborsMatrix;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;

    public BFS(ArrayList<NodeModel> myNodes, int startIndex, int endIndex, double[][] neighborsMatrix, GraphPainter graphPainter) {
        this.nodesList = myNodes;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        visitedTable = new int [myNodes.size()];
        costTable = new BigDecimal [myNodes.size()];
        this.neighborsMatrix = neighborsMatrix;
        this.graphPainter = graphPainter;
        
    }

    public AlgorithmResult run(){
    	
    	long startTime = System.nanoTime();
    	
        for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        costTable[startIndex] = BigDecimal.valueOf(0d);
        
        int actualNode;
        
        queue.add(startIndex);
        visitedTable[startIndex] = startIndex;
        while(queue.size()!=0){
            actualNode = queue.poll();
            graphPainter.paintNodeAsActualNode(nodesList.get(actualNode).getName());
            for(int i = 0; i < nodesList.size(); i++){
                if(neighborsMatrix[actualNode][i] > 0){
                    if(visitedTable[i] == -1){
                    	visitedTable[i] = actualNode;
                    	costTable[i] = costTable[actualNode].add(BigDecimal.valueOf(neighborsMatrix[actualNode][i]));
                        queue.add(i);
                        graphPainter.paintEdge(nodesList.get(actualNode).getName(), nodesList.get(i).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
                        graphPainter.paintNode(nodesList.get(i).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
                    }
                }
            }
            graphPainter.paintNode(nodesList.get(actualNode).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
        }
        visitedTable[startIndex] = -1;
        
        long endTime = System.nanoTime();
        
        graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
		return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);        
    }
}
