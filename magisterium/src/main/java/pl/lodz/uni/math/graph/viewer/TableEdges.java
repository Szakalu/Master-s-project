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

import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.NodeModel;

public class TableEdges extends JFrame {
	
	private TableEdges self;
	
	public TableEdges(double [][] neighborsMatrix, final ArrayList<NodeModel> nodesList, final JFrame mainFrame){
		
		self = this;
		
		//TODO dorobic popupMenu
		JTable tableWithEdges = new JTable();
    	
    	DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
    	tableWithEdges.setModel(defaultTableModel);
    	
    	tableWithEdges.setDefaultEditor(Object.class, null);
    	String header[] = new String[] { "From", "To", "Weight"};     
    	defaultTableModel.setColumnIdentifiers(header);
    	
    	Font f = new Font("Arial", Font.BOLD, 25);
        JTableHeader head = tableWithEdges.getTableHeader();
        head.setFont(f);
        tableWithEdges.setFont(f);
    	
    	for(int row = 0; row < nodesList.size(); row++){
    		for(int column = row; column < nodesList.size(); column++){
    			if(neighborsMatrix[row][column] > 0){
    				defaultTableModel.addRow(new Object[] { nodesList.get(row).getName(), nodesList.get(column).getName(), neighborsMatrix[row][column]});
    			}
    		}
    	}
    	
    	tableWithEdges.setRowHeight(40);
    	
    	tableWithEdges.addMouseListener(new MouseAdapter() {
    	    public void mousePressed(MouseEvent mouseEvent) {
    	        JTable table =(JTable) mouseEvent.getSource();
    	        Point point = mouseEvent.getPoint();
    	        int rowNumber = table.rowAtPoint(point);
    	        if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
    	            self.setEnabled(false);
    	            AddEditEdge addEditEdge = new AddEditEdge(self, rowNumber, 
    	            		new EdgeModel(table.getModel().getValueAt(rowNumber, 0).toString(), table.getModel().getValueAt(rowNumber, 1).toString(), Double.parseDouble(table.getModel().getValueAt(rowNumber, 2).toString())), 
    	            		nodesList);
    	        }
    	    }
    	});
    	
    	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    	centerRenderer.setHorizontalAlignment( JLabel.CENTER );
    	for(int i = 0; i < tableWithEdges.getColumnCount(); ++i){
        	tableWithEdges.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
    	}
    	
    	this.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
				mainFrame.setEnabled(true);
    			dispose();
			}
		});
    	
        this.add(new JScrollPane(tableWithEdges));
        createBottomPanel(this, nodesList);
        
        this.setTitle("Table Edges");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void createBottomPanel(Container mainPanel, final ArrayList<NodeModel> myNodes){
		
		JPanel sidePanel = new JPanel();
		mainPanel.add(sidePanel, BorderLayout.SOUTH);
		sidePanel.setPreferredSize(new Dimension(0, 50));
		
		Button addEdgeButton = new Button("Add Edge");
		addEdgeButton.setPreferredSize(new Dimension(85, 40));
		addEdgeButton.setFont(new Font("Arial", Font.BOLD, 15));
		addEdgeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				self.setEnabled(false);
				AddEditEdge addEditEdge = new AddEditEdge(self, -1, null, myNodes);
			}
		});
		
		sidePanel.add(addEdgeButton);
	}
}
