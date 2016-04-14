package ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class DebuggerWindow extends JFrame {
	
	private InfoPanel shepherdPanel;
	private DebuggerTable populationTable;
	
	public DebuggerWindow() {
		this.initialize();
		this.setVisible(true);
	}
	
	private void initialize(){
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.setBounds(800, 200, 200, 200); 
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//this.setPreferredSize(new Dimension(400, 300));
		
		
		
		shepherdPanel = new InfoPanel(1);
		shepherdPanel.addCell("shepCoord", "Shepherd coordinates: ", "n/a");
		this.add(shepherdPanel);
		
		populationTable = new DebuggerTable();
		this.add(populationTable);
		
		this.pack();
	}
	
	public void setData(String name, Object data){
		shepherdPanel.setData(name, data);
	}
	
	public void resetPopulationData(Object[][] data){
		this.populationTable.resetData(data);
	}
	
	public void editPopulationData(int rowIndex, Object[] data){
		this.populationTable.updateData(rowIndex, data);
	}
}
