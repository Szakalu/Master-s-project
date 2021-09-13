package pl.lodz.uni.math.graph.viewer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import pl.lodz.uni.math.graph.model.NodeModel;



public class TableNodes extends JFrame
{
	private TableNodes self;
	private String starttNode;
	private String stoppNode;
	
    public TableNodes(ArrayList<NodeModel> myNodes, String startNode, String stopNode, final JFrame mainFrame)
    {
    	this.starttNode = startNode;
    	this.stoppNode = stopNode;
    	
    	this.self = this;
    	
    	//TODO dokończyć popupMenu
    	JTable tableWithNodes = new JTable();
    	
    	final DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
    	tableWithNodes.setModel(defaultTableModel);
    	
    	tableWithNodes.setDefaultEditor(Object.class, null);
    	String header[] = new String[] { "Name", "Heuristic", "Start", "Stop"};     
    	defaultTableModel.setColumnIdentifiers(header);
    	
    	Font f = new Font("Arial", Font.BOLD, 25);
        JTableHeader head = tableWithNodes.getTableHeader();
        head.setFont(f);
        tableWithNodes.setFont(f);
    	           
    	for (NodeModel myNode: myNodes) {
    			if(myNode.getName().equals(startNode)){			
    				defaultTableModel.addRow(new Object[] { myNode.getName(), myNode.getHeuristicValue(), "Yes", "No"});	 
    			}
    			else if(myNode.getName().equals(stopNode)){			
    				defaultTableModel.addRow(new Object[] { myNode.getName(), myNode.getHeuristicValue(), "No", "Yes"});	 
    			}
    			else{
    				defaultTableModel.addRow(new Object[] { myNode.getName(), myNode.getHeuristicValue(), "No", "No"});
    			}
    	 }
    	
    	tableWithNodes.setRowHeight(40);
    	
    	tableWithNodes.addMouseListener(new MouseAdapter() {
    	    public void mousePressed(MouseEvent mouseEvent) {
    	        JTable table =(JTable) mouseEvent.getSource();
    	        Point point = mouseEvent.getPoint();
    	        int rowNumber = table.rowAtPoint(point);
    	        if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
    	        	self.setEnabled(false);
    	            AddEditNode addNode = new AddEditNode(self, rowNumber ,new NodeModel(table.getModel().getValueAt(rowNumber, 0).toString(), 
    	            		Double.parseDouble(table.getModel().getValueAt(rowNumber, 1).toString())),
    	            		table.getModel().getValueAt(rowNumber, 0).toString().equals(starttNode),
    	            		table.getModel().getValueAt(rowNumber, 0).toString().equals(stoppNode));
    	        }
    	        else if(mouseEvent.getButton() == 3){
    	        	NodesPopUpMenu nodesPopUpMenupopUpMenu = new NodesPopUpMenu(defaultTableModel, rowNumber);
    	            nodesPopUpMenupopUpMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
    	            System.out.println(((JTable)mouseEvent.getComponent()).getModel().getValueAt(rowNumber, 0));
    	        }
    	    }
    	});
    	
    	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    	centerRenderer.setHorizontalAlignment( JLabel.CENTER );
    	for(int i = 0; i < tableWithNodes.getColumnCount(); ++i){
        	tableWithNodes.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
    	}
    	
    	this.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
				mainFrame.setEnabled(true);
    			dispose();
			}
		});
    	
        this.add(new JScrollPane(tableWithNodes));
        createBottomPanel(this);
        
        this.setTitle("Table Nodes");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void createBottomPanel(Container mainPanel){
		
		JPanel sidePanel = new JPanel();
		mainPanel.add(sidePanel, BorderLayout.SOUTH);
		sidePanel.setPreferredSize(new Dimension(0, 50));
		
		Button addNodeButton = new Button("Add Node");
		addNodeButton.setPreferredSize(new Dimension(85, 40));
		addNodeButton.setFont(new Font("Arial", Font.BOLD, 15));
		addNodeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				self.setEnabled(false);
				AddEditNode addNode = new AddEditNode(self, -1, null, false, false);
			}
		});
		
		sidePanel.add(addNodeButton);
	}
}