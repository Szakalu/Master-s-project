package pl.lodz.uni.math.main;

import java.awt.Container;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.lodz.uni.math.algorithms.AntSystemCycle;
import pl.lodz.uni.math.generator.GraphGenerator;
import pl.lodz.uni.math.generator.HeuristicGenerator;
import pl.lodz.uni.math.graph.model.UndirectedGraphModel;
import pl.lodz.uni.math.graph.viewer.GraphPainter;
import pl.lodz.uni.math.graph.viewer.GraphVisualisation;
import pl.lodz.uni.math.text.file.TextFileManager;

public class Main {
	
	private static String graphPath = "C:\\Users\\Jacek\\Desktop\\Graf.txt";
	private static String bigGraphPath = "C:\\Users\\Jacek\\Desktop\\GrafPelny.txt";
    private static UndirectedGraphModel undirectedGraphModel = new UndirectedGraphModel();
	
	public static void main( String[] args )
    {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		TextFileManager.readGraphFromFile(undirectedGraphModel, graphPath);
		GraphVisualisation graphVisualisation = new GraphVisualisation("Graf", undirectedGraphModel);
		undirectedGraphModel.setGraph(graphVisualisation.getGraph());
		undirectedGraphModel.setGraphViewer(graphVisualisation);
		graphVisualisation.setVisible(true);
		undirectedGraphModel.setStartNode("Gd");//Gd//A
		undirectedGraphModel.setStopNode("Kr");//Kr//F
		//TODO poniżej rozwiązanie gdyby grafy miały mieć możliwość mieć wagi ujemne, czyli dwie osobne macierze 
//		double [][] neigborsMatrix3 = new double [n][n];
//		boolean [][] neighborBoolean = new boolean [n][n];
    }
}
