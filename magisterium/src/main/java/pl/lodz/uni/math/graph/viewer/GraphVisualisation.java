package pl.lodz.uni.math.graph.viewer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.MouseManager;

import pl.lodz.uni.math.algorithms.AlgorithmResult;
import pl.lodz.uni.math.algorithms.AntSystemCycle;
import pl.lodz.uni.math.algorithms.AntSystemDensity;
import pl.lodz.uni.math.algorithms.AntSystemQuantity;
import pl.lodz.uni.math.algorithms.Dijkstra;
import pl.lodz.uni.math.generator.GraphGenerator;
import pl.lodz.uni.math.generator.HeuristicGenerator;
import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.UndirectedGraphModel;
import pl.lodz.uni.math.graph.model.NodeModel;
import pl.lodz.uni.math.graph.viewer.GraphVisualisation;
import pl.lodz.uni.math.text.file.TextFileManager;

public class GraphVisualisation extends JFrame implements KeyListener{
	
	private JTextArea panelForMessageToUser;
	private JScrollPane buttomPanelScroller;
	private JScrollPane scrollerForSidePanel;
	private JTextField messageTextField;
	private JMenuBar menuBar;
	private JMenu file;
	private JMenu settings;
	private JMenu nodeMenu;
	private JMenu edgeMenu;
	private JMenu generator;
	private JCheckBoxMenuItem nodeHeuristic;
	private JCheckBoxMenuItem edgeWeight;
	private JCheckBoxMenuItem automaticPlacement;
	private JMenuItem loadGraphFromFile;
	private JMenuItem saveGraphToFile;
	private JMenuItem nodesTab;
	private JMenuItem edgesTab;
	private JMenuItem graphGenerator;
	private JMenuItem heuristicGenerator;
	private JComboBox<String> algorithmsSelector;
	private JPanel centerPanel;
	private JPanel sidePanel;
	private JLabel stopTimeLabel;
	private JLabel initialPheromonesLabel;
	private JLabel alfaLabel;
	private JLabel betaLabel;
	
	private JTextField stopTimeTextField;
	private JTextField initialPheromonesTextField;
	private JTextField alfaTextField;
	private JTextField betaTextField;
	
	
	
	private Graph graph;
	private DefaultView view;
	private Viewer viewer;
	private String startNode = null;
	private String stopNode = null;
	private Thread algorithmThread;
	
	private UndirectedGraphModel undirectedGraphModel;

	public GraphVisualisation(String title, UndirectedGraphModel undirectedGraphModel) {
			super(title);
			
			this.undirectedGraphModel = undirectedGraphModel;
			
			this.setSize(1280, 720);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			this.graph = new DefaultGraph().takeGraph(undirectedGraphModel.getNeighborsMatrix(), undirectedGraphModel.getNodesList());
//			this.graph.setStrict(false);
//			this.graph.setAutoCreate(true);
			this.graph.addAttribute("ui.quality");
			this.graph.addAttribute("ui.antialias");
			
			this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
			this.viewer.enableAutoLayout();
			this.viewer.addDefaultView(false);
			this.view = (DefaultView) viewer.getDefaultView();
			
			
			
			view.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent mouseEvent) {
					GraphicElement element = view.findNodeOrSpriteAt(mouseEvent.getX(), mouseEvent.getY());
					if(mouseEvent.getButton() == 3){
						printToUser(mouseEvent.toString());
						if(element != null){
							System.out.println(element.getId());	
							printToUser(element.getId());
						}
						else{
							graph.addNode("hej");
							graph.getNode("hej").addAttribute(Settings.UI_LABEL, "hej");
							graph.getNode("hej").addAttribute(Settings.UI_STYLE, Settings.STYL_NORMALNEGO_WIERZCHOLKA);
						}
					}
	    	    }
			});
			
			
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout(10, 10));
			centerPanel.add(view,BorderLayout.CENTER);
			
			this.add(centerPanel);
			
			
			//TODO dodac mouseListener do klikania na Node i menu z niego + klikania na cos innego
			
			JPanel bottomPanel = new JPanel();
			bottomPanel.setLayout(new BorderLayout());
			
			panelForMessageToUser = new JTextArea();
			panelForMessageToUser.setEditable(false);
			buttomPanelScroller = new JScrollPane(panelForMessageToUser);
			buttomPanelScroller.setPreferredSize(new Dimension(0, 100));
			bottomPanel.add(buttomPanelScroller, BorderLayout.CENTER);
			
			createMenuBar(this);
			createSidePanel(this);
			
			messageTextField = new JTextField();
			messageTextField.addKeyListener(this);
			bottomPanel.add(messageTextField, BorderLayout.SOUTH);

			this.add(bottomPanel, BorderLayout.SOUTH);
			setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			
		}

	
	
	public void printToUser(String s) {
		panelForMessageToUser.append(s + "\n");
		buttomPanelScroller.getVerticalScrollBar().setValue(buttomPanelScroller.getVerticalScrollBar().getMaximum());
	}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			clickEnter();
		}
		
	}
	
	public void clickEnter() {
		String messageText = messageTextField.getText();
		String [] splittedMessageText = messageText.split(" ");
		if (messageText.equals("")) {
			return;
		}
		else if(splittedMessageText[0].equals("SetStartNode") && splittedMessageText.length == 2){
			printToUser(messageText);
			undirectedGraphModel.setStartNodeCommand(splittedMessageText[1] + "");
		}
		else if(splittedMessageText[0].equals("SetStopNode") && splittedMessageText.length == 2){
			printToUser(messageText);
			undirectedGraphModel.setStopNodeCommand(splittedMessageText[1] + "");
		}
		else{
			createErrorMessage("Wrong Command: " + messageText, "Wrong Command");
		}
		messageTextField.setText("");
	}
	
	public void createMenuBar(final GraphVisualisation mainFrame){
		menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createSettingsMenu(mainFrame));
		menuBar.add(createGeneratorMenu());
		mainFrame.setJMenuBar(menuBar);
	}
	
	private JMenu createFileMenu(){
		file = new JMenu("File");
		
		saveGraphToFile = new JMenuItem("Save Graph");
		file.add(saveGraphToFile);
		
		saveGraphToFile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooseAndSaveFile();
			}
		});
		
		loadGraphFromFile = new JMenuItem("Load Graph");
		file.add(loadGraphFromFile);
		
		loadGraphFromFile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooseAndLoadFile();
			}
		});
		
		return file;
	}
	
	private JMenu createSettingsMenu(final JFrame mainFrame){
		settings = new JMenu("Settings");
		
		nodeMenu = new JMenu("Nodes");
		
		settings.add(nodeMenu);
		
		nodeHeuristic = new JCheckBoxMenuItem("Heuristic");
		nodeHeuristic.setSelected(false);
		nodeMenu.add(nodeHeuristic);
		
		
		nodeHeuristic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(nodeHeuristic.isSelected()){
					paintNodesWithHeuristic();
				}
				else{
					paintNodesWithoutHeuristic();
				}
				
			}
		});
		
		nodesTab = new JMenuItem("Nodes Table");
		nodeMenu.add(nodesTab);
		
		nodesTab.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				TableNodes tableNodes = new TableNodes(undirectedGraphModel.getNodesList(), getStartNode(), getStopNode(), mainFrame);
				mainFrame.setEnabled(false);
			}
		});
		
		edgeMenu = new JMenu("Edges");
		settings.add(edgeMenu);
		
//		edgeWeight = new JCheckBoxMenuItem("Weight");
//		edgeWeight.setSelected(true);
//		edgeMenu.add(edgeWeight);
//		
//		edgeWeight.addItemListener(new ItemListener() {
//			
//			public void itemStateChanged(ItemEvent arg0) {
//				if(edgeWeight.isSelected()){
//					paintEdgesWithWeight();
//				}
//				else{
//					paintEdgesWithoutWeight();
//				}
//				
//			}
//		});
		
		edgesTab = new JMenuItem("Edges Table");
		edgeMenu.add(edgesTab);
		
		edgesTab.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				TableEdges tableEdges = new TableEdges(undirectedGraphModel.getNeighborsMatrix(), undirectedGraphModel.getNodesList(), mainFrame);
				mainFrame.setEnabled(false);
			}
		});
		
		automaticPlacement = new JCheckBoxMenuItem("Automatic Placement");
		automaticPlacement.setSelected(true);
		settings.add(automaticPlacement);
		
		automaticPlacement.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if(automaticPlacement.isSelected()){
					viewer.enableAutoLayout();
				}
				else{
					viewer.disableAutoLayout();
				}
				
			}
		});
		
		return settings;
	}
	
	private JMenu createGeneratorMenu(){
		generator = new JMenu("Generator");
		
		graphGenerator = new JMenuItem("Graph Generator");
		generator.add(graphGenerator);
		graphGenerator.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//TODO
				
			}
		});
		
		heuristicGenerator = new JMenuItem("Heuristic Generator");
		generator.add(heuristicGenerator);
		
		heuristicGenerator.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//TODO opracowac loading bar
				if(stopNode != null){
					HeuristicGenerator.generateHeuristicForVisualizedGraph(undirectedGraphModel);
					if(nodeHeuristic.isSelected()){
						paintNodesWithHeuristic();
					}
				}
				else{
					createErrorMessage("Please select stop node", "Stop node error");
				}
			}
		});
		return generator;
	}
	
	private void createSidePanel(JFrame mainFrame){
		
		sidePanel = new JPanel();
		sidePanel.setLayout(null);
		sidePanel.setPreferredSize(new Dimension(180, 800));//TODO dopasowac po skonczeniu bocznego paska
		scrollerForSidePanel = new JScrollPane(sidePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainFrame.add(scrollerForSidePanel, BorderLayout.EAST);
		scrollerForSidePanel.setPreferredSize(new Dimension(215, 0));
		String [] choices = new String[9];
		choices[0] = "BFS";
		choices[1] = "DFS";
		choices[2] = "Dijkstra";
		choices[3] = "Bellman-Ford";
		choices[4] = "BestFirstSearch";
		choices[5] = "A*";
		choices[6] = "Ant System Cycle";
		choices[7] = "Ant System Quantity";
		choices[8] = "Ant System Density";
		algorithmsSelector = new JComboBox<String>(choices);
		algorithmsSelector.setBounds(5, 5, 120, 25);
		sidePanel.add(algorithmsSelector);
		
		
		final JButton buttonRunAlgorithm = new JButton("Run");
		buttonRunAlgorithm.setBounds(130, 5, 60, 25);
		sidePanel.add(buttonRunAlgorithm);
		buttonRunAlgorithm.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//TODO napisac blokowanie przycikow i czynnosci po odpaleniu algorytmu
				String selectedAlgorithm = algorithmsSelector.getSelectedItem().toString();
				if(selectedAlgorithm.equals("BFS")){
					algorithmThread = new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runBFS();
						}
					});
					algorithmThread.start();//zeby zastopowac .stop()
				}
				else if(selectedAlgorithm.equals("DFS")){
					new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runDFS();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("Dijkstra")){
					new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runDijkstra();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("Bellman-Ford")){
					new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runBellmanFord();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("BestFirstSearch")){
					new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runBestFirstSearch();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("A*")){
					new Thread(new Runnable() {
						public void run() {
							undirectedGraphModel.runAStar();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("Ant System Quantity")){
					new Thread(new Runnable() {
						public void run() {
							GraphPainter graphPainter = new GraphPainter();
							graphPainter.setGraph(getGraph());
							graphPainter.setStart(getStartNode());
							graphPainter.setStop(getStopNode());
							graphPainter.repaintEdgesToOrginalColors();
							graphPainter.repaintNodesToOrginalColors();
							AntSystemQuantity antSystemQuantity = new AntSystemQuantity(undirectedGraphModel.getNodesList(), undirectedGraphModel.getNeighborsMatrix(), 
									undirectedGraphModel.getNodeIndexNumber(getStartNode()), undirectedGraphModel.getNodeIndexNumber(getStopNode()), 
									graphPainter, 1.2, 2.3, 3, 10, 100, 0.3, 10);
							antSystemQuantity.run();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("Ant System Cycle")){
					new Thread(new Runnable() {
						public void run() {
							GraphPainter graphPainter = new GraphPainter();
							graphPainter.setGraph(getGraph());
							graphPainter.setStart(getStartNode());
							graphPainter.setStop(getStopNode());
							graphPainter.repaintEdgesToOrginalColors();
							graphPainter.repaintNodesToOrginalColors();
							AntSystemCycle antSystemCycle = new AntSystemCycle(undirectedGraphModel.getNodesList(), undirectedGraphModel.getNeighborsMatrix(), 
									undirectedGraphModel.getNodeIndexNumber(getStartNode()), undirectedGraphModel.getNodeIndexNumber(getStopNode()), 
									graphPainter, 1.8, 1.2, 0.1, 15, 10, 0.7, 20);
							antSystemCycle.run();
						}
					}).start();
				}
				else if(selectedAlgorithm.equals("Ant System Density")){
					new Thread(new Runnable() {
						public void run() {
							GraphPainter graphPainter = new GraphPainter();
							graphPainter.setGraph(getGraph());
							graphPainter.setStart(getStartNode());
							graphPainter.setStop(getStopNode());
							graphPainter.repaintEdgesToOrginalColors();
							graphPainter.repaintNodesToOrginalColors();
							AntSystemDensity antSystemDensity = new AntSystemDensity(undirectedGraphModel.getNodesList(), undirectedGraphModel.getNeighborsMatrix(), 
									undirectedGraphModel.getNodeIndexNumber(getStartNode()), undirectedGraphModel.getNodeIndexNumber(getStopNode()), 
									graphPainter, 1.5, 1, 0.1, 10, 20, 0.3, 100);
							antSystemDensity.run();
						}
					}).start();
				}
			}
		});
		
		Font f = new Font("Arial", Font.BOLD, 13);
		
		stopTimeLabel = new JLabel();
		stopTimeLabel.setText("Stop Time");
		stopTimeLabel.setBounds(5, 40, 130, 25);
		stopTimeLabel.setFont(f);
		sidePanel.add(stopTimeLabel);
		
		initialPheromonesLabel = new JLabel();
		initialPheromonesLabel.setText("Initial Pheromones");
		initialPheromonesLabel.setBounds(5, 95, 130, 25);
		initialPheromonesLabel.setFont(f);
		sidePanel.add(initialPheromonesLabel);
		
		alfaLabel = new JLabel();
		alfaLabel.setText("Alfa");
		alfaLabel.setBounds(5, 150, 130, 25);
		alfaLabel.setFont(f);
		sidePanel.add(alfaLabel);
		
		betaLabel = new JLabel();
		betaLabel.setText("Beta");
		betaLabel.setBounds(5, 205, 130, 25);
		betaLabel.setFont(f);
		sidePanel.add(betaLabel);
		
		stopTimeTextField = new JTextField();
		stopTimeTextField.setBounds(5, 60, 188, 25);
		stopTimeTextField.setText(Settings.SLEEP_TIME.toString());
		sidePanel.add(stopTimeTextField);
		
		initialPheromonesTextField = new JTextField();
		initialPheromonesTextField.setText(Settings.INITIAL_PHEROMONES.toString());
		initialPheromonesTextField.setBounds(5, 115, 188, 25);
		sidePanel.add(initialPheromonesTextField);
		
		alfaTextField = new JTextField();
		alfaTextField.setText(Settings.ALFA.toString());
		alfaTextField.setBounds(5, 170, 188, 25);
		sidePanel.add(alfaTextField);
		
		betaTextField = new JTextField();
		betaTextField.setText(Settings.BETA.toString());
		betaTextField.setBounds(5, 225, 188, 25);//225
		sidePanel.add(betaTextField);
	}
	
	private void chooseAndLoadFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to load");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            printToUser("You choose to open this file: " +
                    fileChooser.getSelectedFile().getName());
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            if(fileName.contains(".") && fileName.substring(fileName.lastIndexOf(".")).toString().equals(".txt")){
            	undirectedGraphModel.deleteGraph();
            	undirectedGraphModel.loadAndPaintGraphFromFile(file.getAbsolutePath());
            }
            else{
            	createErrorMessage("Wrong file: " + file.toString() + " Please choose file with .txt extension !!!", "Wrong File");
            }
        }
	}
	
	private void chooseAndSaveFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");   
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fileChooser.setFileFilter(filter);
		int userSelection = fileChooser.showSaveDialog(this);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    String filePathToSave = fileToSave.getAbsolutePath();
		    if(filePathToSave.contains(".")){
		    	if(filePathToSave.substring(filePathToSave.lastIndexOf(".")).toString().equals(".txt")){
			    	if(fileToSave.exists()){
			    		fileToSave.delete();
			    	}
			    }
		    	else{
			    	filePathToSave = filePathToSave+".txt";
			    	if(new File(filePathToSave).exists()){
			    		fileToSave.delete();
			    	}
			    }
		    }
		    else{
		    	filePathToSave = filePathToSave+".txt";
		    	if(new File(filePathToSave).exists()){
		    		fileToSave.delete();
		    	}
		    }
	    	TextFileManager.saveGraphToFile(filePathToSave, undirectedGraphModel);
		}
	}
	
	public void createErrorMessage(String text, String title){
		JOptionPane.showMessageDialog(new JFrame(), text, title, JOptionPane.ERROR_MESSAGE);
	}
	
	
	private void paintNodesWithHeuristic(){
		for(NodeModel myNode: undirectedGraphModel.getNodesList()){
			graph.getNode(myNode.getName()).setAttribute(Settings.UI_LABEL, myNode.getName() + ":" + myNode.getHeuristicValue());
		}
	}
	
	private void paintNodesWithoutHeuristic(){
		for(NodeModel myNode: undirectedGraphModel.getNodesList()){
			graph.getNode(myNode.getName()).setAttribute(Settings.UI_LABEL, myNode.getName());
		}
	}
	
	private void paintEdgesWithWeight(){
		for(Edge edge : graph.getEdgeSet()){
			edge.setAttribute(Settings.UI_LABEL, (String)edge.getAttribute(Settings.UI_WEIGHT));
		}
	}
	
	private void paintEdgesWithoutWeight(){
		for(Edge edge : graph.getEdgeSet()){
			edge.setAttribute(Settings.UI_LABEL, "");
		}
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getStopNode() {
		return stopNode;
	}

	public void setStopNode(String stopNode) {
		this.stopNode = stopNode;
	}
	
	public String getSleepTime(){
		return stopTimeTextField.getText();
	}
	
}

