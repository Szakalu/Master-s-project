package pl.lodz.uni.math.graph.viewer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.Settings;

public class GraphPainter {
	
	private Graph graph;
	private String start;
	private String stop;
	private long sleepTime;
	
	
	
	public GraphPainter() {
	}

	public GraphPainter(Graph graph, String start, String stop, long sleepTime) {
		this.graph = graph;
		this.start = start;
		this.stop = stop;
		this.sleepTime = sleepTime;
	}
	
	public void paintNode(String nodeName, String style){
		if(nodeName.equals(start)){
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, Settings.STYL_STARTOWEGO_WIERZCHOLKA);
		}
		else if(nodeName.equals(stop)){
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, Settings.STYL_KONCOWEGO_WIERZCHOLKA);
		}
		else{
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, style);
		}
		try {
			TimeUnit.NANOSECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNodeWithoutStop(String nodeName, String style){
		if(nodeName.equals(start)){
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, Settings.STYL_STARTOWEGO_WIERZCHOLKA);
		}
		else if(nodeName.equals(stop)){
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, Settings.STYL_KONCOWEGO_WIERZCHOLKA);
		}
		else{
			graph.getNode(nodeName).addAttribute(Settings.UI_STYLE, style);
		}
	}
	
	public void paintNodeAsActualNode(String nodeName){
		graph.getNode(nodeName).setAttribute(Settings.UI_STYLE, Settings.STYL_AKTUALNEGO_WIERZCHOLKA);
		try {
			TimeUnit.NANOSECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void repaintNodesToOrginalColors(){
		for(Node node: graph){
			if(node.getId().equals(start)){
				node.setAttribute(Settings.UI_STYLE, Settings.STYL_STARTOWEGO_WIERZCHOLKA);
			}
			else if(node.getId().equals(stop)){
				node.addAttribute(Settings.UI_STYLE, Settings.STYL_KONCOWEGO_WIERZCHOLKA);
			}
			else{
				node.addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
			}
		}
	}
	
	
	public void paintEdge(String startName, String endName, String style){
		try {
			graph.getEdge(startName + ":" + endName).setAttribute(Settings.UI_STYLE, style);
		} catch (Exception e) {
			try {
				graph.getEdge(endName + ":" + startName).setAttribute(Settings.UI_STYLE, style);
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
	}
	
	public void repaintEdgesToOrginalColors(){
		for(Edge edge: graph.getEdgeSet()){
			edge.setAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEJ_KRAWEDZI);
		}
	}
	
	public void paintRoad(ArrayList<String> road){
		for(int i = 0; i < road.size()-1; ++i){
			paintNodeWithoutStop(road.get(i), Settings.STYL_ODWIEDZONEGO_WIERZCHOLKA);
			paintEdge(road.get(i), road.get(i+1), Settings.STYL_DODAWANEJ_KRAWEDZI_ODWIEDZANEJ_PLUS);
		}
	}

	public static void paintGraph(ArrayList<NodeModel> myNodes, double [][] neighborsMatrix, Graph graph){
    	for(NodeModel myNode: myNodes){
			if(graph.getNode(myNode.getName()) == null){
				graph.addNode(myNode.getName());
				
			}
		}
		
    	for(int row = 0; row < myNodes.size(); row++){
			for(int column = row; column < myNodes.size(); column++){
				if(row != column && neighborsMatrix[row][column] > 0){
					graph.addEdge(myNodes.get(row).getName() + ":" + myNodes.get(column).getName(), myNodes.get(row).getName(), myNodes.get(column).getName());
					graph.getEdge(myNodes.get(row).getName() + ":" + myNodes.get(column).getName()).setAttribute(Settings.UI_WEIGHT, neighborsMatrix[row][column] +"");
				}
			}
		}

		
		for (Node node : graph) {
	        node.addAttribute(Settings.UI_LABEL, node.getId());
	        node.addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
		}
		
		for(Edge edge : graph.getEdgeSet()) {
			edge.addAttribute(Settings.UI_LABEL, (String)edge.getAttribute("weight"));
			edge.addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEJ_KRAWEDZI);			
		}
    }
	
	public void paintEdgeWithActualPheromone(String startName, String endName, double pheromoneValue){
		Edge edge;
		DecimalFormat decimalFormat = new DecimalFormat("#.#######");
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	    decimalFormatSymbols.setDecimalSeparator('.');
	    decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		try {
			edge = graph.getEdge(startName + ":" + endName);
			edge.setAttribute(Settings.UI_LABEL, Double.parseDouble(decimalFormat.format(pheromoneValue)) + " : " +  edge.getAttribute(Settings.UI_WEIGHT));
		} catch (Exception e) {
			try {
				edge = graph.getEdge(endName + ":" + startName);
				edge.setAttribute(Settings.UI_LABEL, Double.parseDouble(decimalFormat.format(pheromoneValue)) + " : " + edge.getAttribute(Settings.UI_WEIGHT));
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	
}
