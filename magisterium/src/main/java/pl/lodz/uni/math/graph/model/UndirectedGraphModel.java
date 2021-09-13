package pl.lodz.uni.math.graph.model;

import pl.lodz.uni.math.algorithms.*;
import pl.lodz.uni.math.graph.viewer.Settings;
import pl.lodz.uni.math.graph.viewer.GraphPainter;
import pl.lodz.uni.math.graph.viewer.GraphVisualisation;
import pl.lodz.uni.math.text.file.TextFileManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.graphstream.graph.Graph;


public class UndirectedGraphModel {

    private ArrayList<NodeModel> nodesList = new ArrayList<NodeModel>();
    //private ArrayList<EdgeModel> myEdges = new ArrayList<EdgeModel>();
    private double [][] neighborsMatrix = new double[0][0];
    private int graphNumber = 0;
    private String startNode = null;
    private String stopNode = null;
    private Graph graph;
    private GraphVisualisation graphVisualisation;
    private GraphPainter graphPainter = new GraphPainter();
    
    public UndirectedGraphModel() {
    }
    
    public void setStartNodeCommand(String startNode) {
        if(getNodeIndexNumber(startNode) == -1){
        	graphVisualisation.printToUser("Wrong start node name: " + startNode);
        	graphVisualisation.createErrorMessage("Wrong start node name: " + startNode, "Error");
        }
        else {
        	setStartNode(startNode);
        }
        
    }
    
    public void setStopNodeCommand(String stopNode){
    	if(getNodeIndexNumber(stopNode) == -1){
    		graphVisualisation.printToUser("Wrong stop node name: " + stopNode);
        	graphVisualisation.createErrorMessage("Wrong stop node name: " + stopNode, "Error");
        }
    	else{
    		setStopNode(stopNode);
    	}
    }

    public void setStartNode(String startNode) {
    	if(getStartNode() != null){
    		graph.getNode(getStartNode()).addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
    	}
    	if(getStopNode() != null){
    		if(getStopNode().equals(startNode)){
        		this.stopNode = null;
        		graphVisualisation.setStopNode(null);
    		}
    	}
    	this.startNode = startNode;
    	graph.getNode(startNode).setAttribute(Settings.UI_STYLE, Settings.STYL_STARTOWEGO_WIERZCHOLKA);
    	graphVisualisation.setStartNode(startNode);
        
    }

    public String getStopNode() {
        return stopNode;
    }

    public void setStopNode(String stopNode) {	
		if(getStopNode() != null){
			graph.getNode(getStopNode()).addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
		}
		if(getStartNode() != null){
			if(getStartNode().equals(stopNode)){
    			this.startNode = null;
    			graphVisualisation.setStartNode(null);
			}
		}
		this.stopNode = stopNode;
		graph.getNode(getStopNode()).setAttribute(Settings.UI_STYLE, Settings.STYL_KONCOWEGO_WIERZCHOLKA);
		graphVisualisation.setStopNode(stopNode);
	
    }

    //TODO poprawic układ pliku na index index nazwa nazwa waga
    public void addEdge(EdgeModel edge){// do zmiany w czasie po dodaniu krawedzi odwrotnej np z 1->2 na 2->1 nie zmieniony zostaje indeks w wizualizacji grafu
    	//TODO Metoda dodawania krawedzi dla uporzadkowanego pliku wejsciowego postaci index1 index2 nazwa1 nazwa2 waga
//    	if(myEdge.getStartName().equals(nodesList.get(myEdge.getStartIndex()).getName())){
//    		if(!checkIfEdgeExist(myEdge)){
//    			myEdges.add(myEdge);
//    			if(myEdge.getEndName().equals("5999")){
//                    System.out.println("Dodana krawędz: " + myEdge.getStartName() + " -> " + myEdge.getEndName());
//                }
//    		}
//    	}
    	
    	//TODO Metoda dodawania dla zczytania z pliku nie uporzadkowanego
    	if(edge.getWeight() <= 0){
    		return;
    	}
    	for (NodeModel n: nodesList) {
	          if(edge.getStartIndex() == -1 && edge.getStartName().equals(n.getName())){
	              edge.setStartIndex(n.getNumber());
	          }
	          else if(edge.getEndIndex() == -1 && edge.getEndName().equals(n.getName())){
	              edge.setEndIndex(n.getNumber());
	          }
	          if(edge.getStartIndex()!= -1 && edge.getEndIndex() != -1){
	              if(!checkIfEdgeExist(edge)){
		              neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()] = edge.getWeight();
		              neighborsMatrix[edge.getEndIndex()][edge.getStartIndex()] = neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()];
	              }
	              return;
	          }
    	}
    }
    
    private boolean checkIfEdgeExist(EdgeModel edge){
    	if(neighborsMatrix[edge.getStartIndex()][edge.getEndIndex()] > 0){
    		return true;
    	}
    	return false;
    }

    public void removeEdge(EdgeModel myEdge){
        refactorNeighborTableEdgeRemove(myEdge);
    }

    //TODO napisać inne dodawanie wierzchołków nie z pliku
    public void addNodeFromFile(NodeModel myNode){
        if(myNode.getHeuristicValue() < 0){
        	return;
        }
    	for (NodeModel n: nodesList) {
            if(n.getName().equals(myNode.getName())){
                return;
            }
        }
        myNode.setNumber(graphNumber);
        raiseGraphNumber();
        nodesList.add(myNode);
        System.out.println("Dodany wierzchołek: " + myNode.getName());
    }

//    public void removeNode(NodeModel myNode){
//        int nodePosition = getNodePosition(myNode);
//        ArrayList<Integer> edgesToRemove = new ArrayList<Integer>();
//        if(nodePosition != -1){
//            if(myEdges.size() > 0){
//                for(int i = 0; i < myEdges.size(); ++i){
//                    if(myEdges.get(i).getStartName().equals(myNode.getName()) || myEdges.get(i).getEndName().equals(myNode.getName())){
//                        edgesToRemove.add(i);
//                    }
//                }
//
//                int indexMinus = 0;
//                for (int i = 0; i < edgesToRemove.size(); i++) {
//                    myEdges.remove((edgesToRemove.get(i)-indexMinus));
//                    indexMinus++;
//                }
//            }
//
//            int nodeNumber = nodesList.get(nodePosition).getNumber();
//            nodesList.remove(nodePosition);
//            for(NodeModel n: nodesList){
//                if(n.getNumber()>nodeNumber){
//                    n.setNumber(n.getNumber()-1);
//                }
//            }
//            downGraphNumber();
//            
//        }
//    }

    private int getNodePosition(NodeModel myNode){
        for (int i = 0; i < nodesList.size(); i++) {
            if(nodesList.get(i).getName().equals(myNode.getName())){
                return i;
            }
        }
        return -1;
    }

    private void raiseGraphNumber(){
        graphNumber++;
    }

    private void downGraphNumber(){
        graphNumber--;
    }

    public void printAllNodes(){
        for (NodeModel n: nodesList) {
            System.out.println(n.toString());
        }
    }

    public  void  printAllEdges(){
        for(int row = 0; row < nodesList.size(); row++){
        	for(int column = row; column < nodesList.size(); column++){
        		if(row != column && neighborsMatrix[row][column] > 0){
        			System.out.println(nodesList.get(row).getName() + " -> " + nodesList.get(column).getName() + " " + neighborsMatrix[row][column]);
        		}
            }
        }
    }

    public void runBFS(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        BFS bfs = new BFS(getNodesList(), getNodeIndexNumber(startNode),getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
        AlgorithmResult bfsResult = bfs.run();
        createAndPaintRoadIfExist(bfsResult.getVisitedTable(), bfsResult.getCostTable(), bfsResult.getStartIndex(), bfsResult.getEndIndex(), bfsResult.getTime());
    }

    public void runDFS(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        DFS dfs = new DFS(nodesList, getNodeIndexNumber(startNode), getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
        AlgorithmResult dfsResult = dfs.run();
        createAndPaintRoadIfExist(dfsResult.getVisitedTable(), dfsResult.getCostTable(), dfsResult.getStartIndex(), dfsResult.getEndIndex(), dfsResult.getTime());
    }

    public void runDijkstra(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        Dijkstra dijkstra = new Dijkstra(nodesList, getNodeIndexNumber(startNode), getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
        AlgorithmResult dijkstraResult = dijkstra.run();
        createAndPaintRoadIfExist(dijkstraResult.getVisitedTable(), dijkstraResult.getCostTable(), dijkstraResult.getStartIndex(), dijkstraResult.getEndIndex(), dijkstraResult.getTime());
    }

    public void runBellmanFord(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        BellmanFord bellmanFord = new BellmanFord(nodesList, getNodeIndexNumber(startNode), getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
        AlgorithmResult bellmanFordResult = bellmanFord.run();
        createAndPaintRoadIfExist(bellmanFordResult.getVisitedTable(), bellmanFordResult.getCostTable(), bellmanFordResult.getStartIndex(), bellmanFordResult.getEndIndex(), bellmanFordResult.getTime());
    }

    public void runBestFirstSearch(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        BestFirstSearch bestFirstSearch = new BestFirstSearch(nodesList, getNodeIndexNumber(startNode), getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
        AlgorithmResult bestFirstSearchResult = bestFirstSearch.run();
        createAndPaintRoadIfExist(bestFirstSearchResult.getVisitedTable(), bestFirstSearchResult.getCostTable(), bestFirstSearchResult.getStartIndex(), bestFirstSearchResult.getEndIndex(), bestFirstSearchResult.getTime());
    }
    
    public void runAStar(){
    	setGraphPainter();
    	graphPainter.repaintEdgesToOrginalColors();
    	graphPainter.repaintNodesToOrginalColors();
        AStar aStar = new AStar(nodesList, getNodeIndexNumber(startNode), getNodeIndexNumber(stopNode), neighborsMatrix, graphPainter);
    	AlgorithmResult aStarResult= aStar.run();
    	createAndPaintRoadIfExist(aStarResult.getVisitedTable(), aStarResult.getCostTable(), aStarResult.getStartIndex(), aStarResult.getEndIndex(), aStarResult.getTime());
    }
    
    private void createAndPaintRoadIfExist(int [] visitedTable, BigDecimal [] costTable, int startIndex, int endIndex, long time){
    	if(visitedTable[endIndex] != -1 && checkIfRoadExists(startIndex, endIndex, visitedTable)){
        	ArrayList<String> road = new ArrayList<String>();
        	int actualNode = endIndex;
        	while(actualNode != -1){
        		road.add(0, nodesList.get(actualNode).getName());
        		actualNode = visitedTable[actualNode];
        	}
        	graphPainter.paintRoad(road);
        	graphVisualisation.printToUser("Road from " + nodesList.get(startIndex).getName() + " to " + nodesList.get(endIndex).getName() + ":");
        	StringBuilder nodesRoad = new StringBuilder();
        	for(String nodeName: road){
        		nodesRoad.append(nodeName + " -> ");
        	}
        	nodesRoad.delete(nodesRoad.length()-4, nodesRoad.length()-1);
        	graphVisualisation.printToUser(nodesRoad.toString());
        	graphVisualisation.printToUser("Road Cost: " +  costTable[endIndex]);
        	DecimalFormat decimalFormat = new DecimalFormat("#.#####");
    		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    	    decimalFormatSymbols.setDecimalSeparator('.');
    	    decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    	    graphVisualisation.printToUser("Algorithm Time: " + Double.parseDouble(decimalFormat.format(((double)time)/1000000000)) + " seconds");
    	}
        else{
        	graphVisualisation.printToUser("You can't reach " + nodesList.get(endIndex).getName() + " from " + nodesList.get(startIndex).getName());
        }
    }
    
    private boolean checkIfRoadExists(int startIndex, int endIndex, int [] visitedTable){
    	int actualNode = endIndex;
    	while(actualNode != -1 && actualNode != startIndex){
    		actualNode = visitedTable[actualNode];
    	}
    	if(actualNode == startIndex){
    		return true;
    	}
    	return false;
    }

    public int getNodeIndexNumber(String nodeName){
        for (int i = 0; i < nodesList.size(); i++) {
            if(nodesList.get(i).getName().equals(nodeName)){
                return i;
            }
        }
        return -1;
    }

    private void refactorNeighborTableEdgeAdd(EdgeModel myEdge){
        neighborsMatrix[myEdge.getStartIndex()][myEdge.getEndIndex()] = myEdge.getWeight();
        neighborsMatrix[myEdge.getEndIndex()][myEdge.getStartIndex()] = myEdge.getWeight();
    }

    private void refactorNeighborTableEdgeRemove(EdgeModel myEdge){
        neighborsMatrix[myEdge.getStartIndex()][myEdge.getEndIndex()] = 0;
        neighborsMatrix[myEdge.getEndIndex()][myEdge.getStartIndex()] = 0;
    }
    
    public void refactorNeighborTableCreate(){
        neighborsMatrix = new double[nodesList.size()][nodesList.size()];
    }
    
    public void deleteGraph(){
    	for(int row = 0; row < nodesList.size(); row++){
    		for(int column = row; column < nodesList.size(); column++){
        		if(row != column && neighborsMatrix[row][column] > 0){
        			graph.removeEdge(nodesList.get(row).getName()+ ":" + nodesList.get(column).getName());
        		}
        	}
    	}
    	for(NodeModel myNode: getNodesList()){
    		graph.removeNode(myNode.getName());
    	}
    	getNodesList().clear();
    	neighborsMatrix = new double[0][0];
        graphNumber = 0;
        startNode = null;
        stopNode = null;
        graphVisualisation.setStartNode(null);
        graphVisualisation.setStopNode(null);
    }
    
    public void loadAndPaintGraphFromFile(String graphPath){
    	TextFileManager.readGraphFromFile(this, graphPath);
    	GraphPainter.paintGraph(getNodesList(), getNeighborsMatrix(), getGraph());

    }
    
    public String getStartNode() {
        return startNode;
    }
    
    public ArrayList<NodeModel> getNodesList() {
        return nodesList;
    }

    public void setNodesList(ArrayList<NodeModel> nodesList) {
        this.nodesList = nodesList;
    }

	public double[][] getNeighborsMatrix() {
		return neighborsMatrix;
	}

	public void setNeighborsMatrix(double[][] neighborsMatrix) {
		this.neighborsMatrix = neighborsMatrix;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public GraphVisualisation getGraphViewer() {
		return graphVisualisation;
	}

	public void setGraphViewer(GraphVisualisation graphVisualisation) {
		this.graphVisualisation = graphVisualisation;
	}

	public GraphPainter getGraphPainter() {
		return graphPainter;
	}

	public void setGraphPainter() {
		graphPainter.setGraph(getGraph());
		graphPainter.setStart(getStartNode());
		graphPainter.setStop(getStopNode());
		graphPainter.setSleepTime((long) (Double.parseDouble(graphVisualisation.getSleepTime()) * 1000000000));
	}

	

	
	
}
