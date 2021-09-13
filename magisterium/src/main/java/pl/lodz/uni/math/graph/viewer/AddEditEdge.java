package pl.lodz.uni.math.graph.viewer;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import pl.lodz.uni.math.graph.model.EdgeModel;
import pl.lodz.uni.math.graph.model.NodeModel;

public class AddEditEdge extends JFrame {
    private JButton submitButton;
    private JLabel fromLabel;
    private JComboBox<String> fromComboBox;
    private JLabel toLabel;
    private JComboBox<String> toComboBox;
    private JLabel weightLabel;
    private JTextField weightTextField;
    
    private int startNodePosition = -1;
    private int endNodePosition =  -1;
    
    public AddEditEdge(final JFrame parentFrame, int rowNumberOfEdge, EdgeModel myEdge, ArrayList<NodeModel> myNodes) {
        //construct preComponents
        String[] fromComboBoxItems = new String[myNodes.size()];
        String[] toComboBoxItems = new String[myNodes.size()];
        
        if(rowNumberOfEdge == -1){
        	for(int i = 0; i < myNodes.size(); ++i){
            	fromComboBoxItems[i] = myNodes.get(i).getName();
            	toComboBoxItems[i] = myNodes.get(i).getName();
            }
        }
        else{
        	for(int i = 0; i < myNodes.size(); ++i){
            	fromComboBoxItems[i] = myNodes.get(i).getName();
            	toComboBoxItems[i] = myNodes.get(i).getName();
            	if(startNodePosition != -1 && myNodes.get(i).getName().equals(myEdge.getStartName())){
            		startNodePosition = i;
            	}
            	if(endNodePosition != -1 && myNodes.get(i).getName().equals(myEdge.getEndName())){
            		endNodePosition = i;
            	}
            }
        }

        JPanel mainPanel = new JPanel();
        submitButton = new JButton ("Submit");
        fromLabel = new JLabel ("From:");
        fromComboBox = new JComboBox<String> (fromComboBoxItems);
        toLabel = new JLabel ("To:");
        toComboBox = new JComboBox<String> (toComboBoxItems);
        weightLabel = new JLabel ("Weight:");
        weightTextField = new JTextField (5);

        mainPanel.setPreferredSize(new Dimension (603, 158));
        mainPanel.setLayout(null);

        mainPanel.add(submitButton);
        mainPanel.add(fromLabel);
        mainPanel.add(fromComboBox);
        mainPanel.add(toLabel);
        mainPanel.add(toComboBox);
        mainPanel.add(weightLabel);
        mainPanel.add(weightTextField);

        submitButton.setBounds (265, 110, 100, 25);
        fromLabel.setBounds (75, 55, 40, 25);
        fromComboBox.setBounds (110, 55, 100, 25);
        toLabel.setBounds (245, 55, 25, 25);
        toComboBox.setBounds (265, 55, 100, 25);
        weightLabel.setBounds (395, 55, 45, 25);
        weightTextField.setBounds (440, 55, 100, 25);
        
        if(rowNumberOfEdge == -1){
        	this.setTitle("Add Edge");
      	  	submitButton.setText("Add Edge");
        }
        else{
      	  this.setTitle("Edit Edge");
      	  UIManager.put( "ComboBox.disabledForeground", Color.BLACK );
      	  submitButton.setText("Edit Edge");
      	  fromComboBox.setSelectedItem(myEdge.getStartName());
      	  fromComboBox.setEnabled(false);
      	  System.out.println(fromComboBox.getBackground().toString());
      	  toComboBox.setSelectedItem(myEdge.getEndName());
      	  toComboBox.setEnabled(false);
      	  weightTextField.setText(String.valueOf(myEdge.getWeight()));
        }
        
        //TODO dorobic logike przycisku submitButton + zadbac o odpowiednie wlaczenie okienka rodzica
        
        this.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
				parentFrame.setEnabled(true);
    			dispose();
			}
		});
        
        this.add(mainPanel);
        this.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible (true);
    }
}