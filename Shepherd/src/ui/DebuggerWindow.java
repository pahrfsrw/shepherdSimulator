package ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class DebuggerWindow extends JFrame {
	
	private InfoPanel shepherdPanel;
	
	public DebuggerWindow() {
		this.initialize();
		this.setVisible(true);
	}
	
	private void initialize(){
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.setBounds(800, 200, 200, 200); 
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(300, 300));
		
		this.pack();
		
		shepherdPanel = new InfoPanel(1);
		shepherdPanel.addCell("shepCoord", "Shepherd coordinates: ", "n/a");
		this.add(shepherdPanel);
	}
	
	public void setData(String name, Object data){
		shepherdPanel.setData(name, data);
	}
}
