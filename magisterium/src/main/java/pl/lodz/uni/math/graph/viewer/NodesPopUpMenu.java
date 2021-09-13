package pl.lodz.uni.math.graph.viewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

class NodesPopUpMenu extends JPopupMenu {
    
	private JMenuItem deleteNodeItem;
    
    public NodesPopUpMenu(DefaultTableModel tableNodesModel, int rowNumberOfNode) {
    	Font fontForMenuItems = new Font("Arial", Font.BOLD, 15);
    	deleteNodeItem = createDeleteNodeMenuItem(fontForMenuItems, tableNodesModel, rowNumberOfNode);
    	
    	
        this.add(deleteNodeItem);
        
    }
    
    public JMenuItem createDeleteNodeMenuItem(Font font, final DefaultTableModel tableNodesModel, final int rowNumberOfNode){
    	deleteNodeItem = new JMenuItem("Delete Node");
    	deleteNodeItem.setFont(font);
    	
    	deleteNodeItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
		    	tableNodesModel.removeRow(rowNumberOfNode);
		    	//TODO mozna zamienic na uruchamianie funkcji deleteNode() w klasie TableNodes (odsylanie wiadomosci)
			}
		});
    	
    	return deleteNodeItem;
    }
}
