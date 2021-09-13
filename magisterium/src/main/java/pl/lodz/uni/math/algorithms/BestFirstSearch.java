package pl.lodz.uni.math.algorithms;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BestFirstSearch {

    private ArrayList<NodeModel> nodesList;
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private double [][] neighborTable;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;

    public BestFirstSearch(ArrayList<NodeModel> myNodes, int startIndex, int endIndex, double[][] neighborTable, GraphPainter graphPainter) {
        this.nodesList = myNodes;
        this.neighborTable = neighborTable;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.visitedTable = new int [myNodes.size()];
        this.costTable = new BigDecimal[myNodes.size()];
        this.graphPainter = graphPainter;
    }

    public AlgorithmResult run(){

    	long startTime = System.nanoTime();
    	
        for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        costTable[startIndex] = BigDecimal.valueOf(0d);
        visitedTable[startIndex] = startIndex;

        ArrayList<NodeModel> openList = new ArrayList<NodeModel>();

        openList.add(nodesList.get(startIndex));
        NodeModel actualMyNode;
        while(openList.size()>0){
            actualMyNode = openList.get(0);
            openList.remove(0);
            graphPainter.paintNodeAsActualNode(actualMyNode.getName());
            if(actualMyNode.getNumber() == endIndex){
                break;
            }
            for(int i = 0; i < nodesList.size(); i++){
                if(neighborTable[actualMyNode.getNumber()][i] > 0 && visitedTable[i] == -1){
                	graphPainter.paintEdge(actualMyNode.getName(), nodesList.get(i).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
                    graphPainter.paintNode(nodesList.get(i).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
                	visitedTable[i] = actualMyNode.getNumber();
                    costTable[i] = costTable[actualMyNode.getNumber()].add(BigDecimal.valueOf(neighborTable[actualMyNode.getNumber()][i]));
                    addNodeToHeuristicPriorityList(openList, i);
                }
            }
            graphPainter.paintNode(actualMyNode.getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
        }
        visitedTable[startIndex] = -1;

        long endTime = System.nanoTime();
        
        graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
        return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);        

    }

    private void addNodeToHeuristicPriorityList(ArrayList<NodeModel> heuristicPriorityList, int nodeIndex){
        NodeModel node = nodesList.get(nodeIndex);
        if(heuristicPriorityList.size() == 0){
            heuristicPriorityList.add(node);
            return;
        }
        else{
            for(int i = 0; i < heuristicPriorityList.size();++i){
                if(heuristicPriorityList.get(i).getHeuristicValue() > node.getHeuristicValue()){
                    heuristicPriorityList.add(i, node);
                    return;
                }
            }
            heuristicPriorityList.add(node);
        }
    }
}
