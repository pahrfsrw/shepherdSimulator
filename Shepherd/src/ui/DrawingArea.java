package ui;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import simulation.EntityManager;

// Áður: DrawingArea extends JPanel
class DrawingArea extends JPanel {

	DrawingArea() {

	}
	
	void drawInfo(Graphics g, int gen, double time){
		g.setColor(Color.BLACK);
		g.drawString("Generation: " + gen, 10, 530);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.BLUE);
		this.setOpaque(true);
		
		EntityManager em = EntityManager.getInstance();
		
		// Draw everything
		em.draw(g);
		
		// Information
		drawInfo(g, 10, 10.0);
	}

}
