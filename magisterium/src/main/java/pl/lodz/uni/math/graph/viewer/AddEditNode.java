package pl.lodz.uni.math.graph.viewer;
//Generated by GuiGenie - Copyright (c) 2004 Mario Awad.
//Home Page http://guigenie.cjb.net - Check often for new versions!

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import pl.lodz.uni.math.graph.model.NodeModel;

public class AddEditNode extends JFrame {
  private JButton submitButton;
  private JTextField nodeNameTextField;
  private JLabel nodeNameLabel;
  private JTextField nodeHeuristicValueTextField;
  private JLabel nodeHeuristicValueLabel;
  private JCheckBox startNodeCheckBox;
  private JCheckBox stopNodeCheckBox;

  public AddEditNode(final JFrame parentFrame, int rowNumberOfNode, NodeModel myNode, boolean isStart, boolean isStop) {
	  
	  JPanel mainPanel = new JPanel();
      //construct components
      submitButton = new JButton ("Submit");
      nodeNameTextField = new JTextField (5);
      nodeNameLabel = new JLabel ("Name:");
      nodeHeuristicValueTextField = new JTextField (5);
      nodeHeuristicValueLabel = new JLabel ("Heuristic Value:");
      startNodeCheckBox = new JCheckBox ("Start");
      stopNodeCheckBox = new JCheckBox ("Stop");

      //adjust size and set layout
      mainPanel.setPreferredSize (new Dimension (596, 107));
      mainPanel.setLayout (null);
      

      //add components
      mainPanel.add(submitButton);
      mainPanel.add(nodeNameTextField);
      mainPanel.add(nodeNameLabel);
      mainPanel.add(nodeHeuristicValueTextField);
      mainPanel.add(nodeHeuristicValueLabel);
      mainPanel.add(startNodeCheckBox);
      mainPanel.add(stopNodeCheckBox);

      //set component bounds (only needed by Absolute Positioning)
      submitButton.setBounds (245, 75, 110, 25);
      nodeNameTextField.setBounds (105, 30, 100, 25);
      nodeNameLabel.setBounds (60, 30, 45, 25);
      nodeHeuristicValueTextField.setBounds (320, 30, 100, 25);
      nodeHeuristicValueLabel.setBounds (225, 30, 95, 25);
      startNodeCheckBox.setBounds (440, 30, 60, 25);
      stopNodeCheckBox.setBounds (515, 30, 60, 25);
      
      if(isStart){
    	  startNodeCheckBox.setSelected(true);
      }
      if(isStop){
    	  stopNodeCheckBox.setSelected(true);
      }
      
      if(rowNumberOfNode == -1){
    	  this.setTitle("Add Node");
    	  submitButton.setText("Add Node");
      }
      else{
    	  this.setTitle("Edit Node");
    	  submitButton.setText("Edit Node");
    	  nodeNameTextField.setText(myNode.getName());
    	  nodeNameTextField.setEnabled(false);
    	  nodeNameTextField.setBackground(new ColorUIResource(238, 238, 238));
    	  nodeNameTextField.setDisabledTextColor(Color.BLACK);
    	  nodeHeuristicValueTextField.setText(String.valueOf(myNode.getHeuristicValue()));
      }
      
      startNodeCheckBox.addItemListener(new ItemListener() {
		
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(stopNodeCheckBox.isSelected()){
					stopNodeCheckBox.setSelected(false);	
				}
			}
		}
      });
      
      stopNodeCheckBox.addItemListener(new ItemListener() {
  		
  		public void itemStateChanged(ItemEvent e) {
  			if(e.getStateChange() == ItemEvent.SELECTED) {
  				if(startNodeCheckBox.isSelected()){
  					startNodeCheckBox.setSelected(false);	
  				}
  			}
  		}
      });
      
      //TODO dorobic logike submitButton
      
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