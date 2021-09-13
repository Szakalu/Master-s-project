package pl.lodz.uni.math.graph.viewer;

import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.UndirectedGraphModel;
import pl.lodz.uni.math.graph.model.NodeModel;

public class DefaultGraph {
	

	public Graph takeGraph(double [][] neighborsMatrix, ArrayList<NodeModel> myNodes){
		Graph graph = new SingleGraph("Graph");
//		graph.setStrict(true);
//		graph.setAutoCreate( false );

		for(NodeModel myNode: myNodes){
			graph.addNode(myNode.getName());
		}
		
		for(int row = 0; row < myNodes.size(); row++){
			for(int column = row; column < myNodes.size(); column++){
				if(row != column && neighborsMatrix[row][column] > 0){
					graph.addEdge(myNodes.get(row).getName() + ":" + myNodes.get(column).getName(), myNodes.get(row).getName(), myNodes.get(column).getName());
					graph.getEdge(myNodes.get(row).getName() + ":" + myNodes.get(column).getName()).setAttribute(Settings.UI_WEIGHT, neighborsMatrix[row][column] +"");
				}
			}
		}

		for (Node node : graph.getNodeSet()) {
	        node.addAttribute(Settings.UI_LABEL, node.getId());
	        node.addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
		}
		
		for(Edge edge : graph.getEdgeSet()) {
			edge.addAttribute(Settings.UI_LABEL, (String)edge.getAttribute("weight"));
			edge.addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEJ_KRAWEDZI);
		}
		return graph;
	}

}
