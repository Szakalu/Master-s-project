package pl.lodz.uni.math.algorithms;

import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Dijkstra {

    private ArrayList<NodeModel> myNodes;
    private int [] visitedTable;
    private BigDecimal [] costTable;
    private double [][] neighborTable;
    private int startIndex;
    private int endIndex;
    private GraphPainter graphPainter;

    public Dijkstra(ArrayList<NodeModel> myNodes, int startIndex, int endIndex, double[][] neighborTable, GraphPainter graphPainter) {
        this.myNodes = myNodes;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.visitedTable = new int [myNodes.size()];
        this.costTable = new BigDecimal[myNodes.size()];
        this.neighborTable = neighborTable;
        this.graphPainter = graphPainter;
    }
    
    public Dijkstra(){
    	
    }

    public AlgorithmResult run(){
    	
    	long startTime = System.nanoTime();

        for(int i = 0; i < visitedTable.length; i++){
            visitedTable[i] = -1;
            costTable[i] = BigDecimal.valueOf(Double.MAX_VALUE);
        }

        ArrayList<NodeModel> nodesToCheck = new ArrayList<NodeModel>(myNodes);

        costTable[startIndex] = BigDecimal.valueOf(0d);
        visitedTable[startIndex] = startIndex;

        BigDecimal lowestCost;
        int actualNode;

        while(nodesToCheck.size() != 0){
        	
        	//TODO poprawic wyszukiwanie najmniejszego wierzchołka, wyrzucic usowanie w dodatkowej funkcji (funkcja sprawdzajaca zwaraca indeks na liscie nodesToCheck)
        	
            lowestCost = costTable[nodesToCheck.get(0).getNumber()];
            actualNode = nodesToCheck.get(0).getNumber();
            for(NodeModel n: nodesToCheck){
                if(costTable[n.getNumber()].compareTo(lowestCost) < 0){
                    lowestCost = costTable[n.getNumber()];
                    actualNode = n.getNumber();
                }
            }
            
            removeActualNodeFromNodesToCheck(nodesToCheck, actualNode);
            graphPainter.paintNodeAsActualNode(myNodes.get(actualNode).getName());
            
            for(int i = 0; i < myNodes.size(); i++){
                if(neighborTable[actualNode][i] == 0){
                    continue;
                }
                if(costTable[i].compareTo(costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][i]))) > 0){
                //if(costTable[i] > costTable[actualNode]+edgeWeight){
                	if(visitedTable[i] != -1){
                		graphPainter.paintEdge(myNodes.get(i).getName(), myNodes.get(visitedTable[i]).getName(), Settings.STYL_NORMALNEJ_KRAWEDZI);
                	}
                	graphPainter.paintEdge(myNodes.get(actualNode).getName(), myNodes.get(i).getName(), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
                	graphPainter.paintNode(myNodes.get(i).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA_Z_SASIADEM);
                    costTable[i] = costTable[actualNode].add(BigDecimal.valueOf(neighborTable[actualNode][i]));
                	//costTable[i] = costTable[actualNode]+edgeWeight;
                    visitedTable[i] = actualNode;
                    
                }
            }
            graphPainter.paintNode(myNodes.get(actualNode).getName(), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
        }
        
        visitedTable[startIndex] = -1;

        long endTime = System.nanoTime();
        
        graphPainter.repaintEdgesToOrginalColors();
        graphPainter.repaintNodesToOrginalColors();
        
        return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);
    }
    

    private void removeActualNodeFromNodesToCheck(ArrayList<NodeModel> nodesToCheck, int actualNode){
        for(int i = 0; i < nodesToCheck.size(); ++i){
            if(nodesToCheck.get(i).getNumber() == actualNode){
                nodesToCheck.remove(i);
                break;
            }
        }
    }
    
    public AlgorithmResult dijkstraRun(ArrayList<Integer> nodesToCheckk, double [][] neighborsMatrix, int n, int startIndex, int endIndex, boolean isStop){
    	
    	
    	long startTime = System.nanoTime();

    	int visitedTable[] = new int [n];
    	BigDecimal costTable [] = new BigDecimal [n];
    	ArrayList<Integer> nodesToCheck = new ArrayList<Integer>(nodesToCheckk);
    	
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
            if(isStop && actualNode == endIndex){
            	break;
            }
            nodesToCheck.remove(actualNodeIndex);
            
            
            for(int i = 0; i < n; i++){
                if(neighborsMatrix[actualNode][i] == 0){
                    continue;
                }
                if(costTable[i].compareTo(costTable[actualNode].add(BigDecimal.valueOf(neighborsMatrix[actualNode][i]))) > 0){
                //if(costTable[i] > costTable[actualNode]+edgeWeight){
                	costTable[i] = costTable[actualNode].add(BigDecimal.valueOf(neighborsMatrix[actualNode][i]));
                	//costTable[i] = costTable[actualNode]+edgeWeight;
                    visitedTable[i] = actualNode;
                    
                }
            }
        }
        
        visitedTable[startIndex] = -1;

        long endTime = System.nanoTime();
        System.out.println("Koniec");
        return new AlgorithmResult(costTable, visitedTable, endTime - startTime, startIndex, endIndex);
    }
    
}
