package ui;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLayeredPane;

class DrawingAreaUnderlay extends JLayeredPane{
	
	DrawingAreaUnderlay() {

	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setOpaque(true); // Annars er þetta ósýnilegt
		this.setBackground(Color.GRAY);
		
		
	}

}
