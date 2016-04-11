package simulation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Meadow {
	
	private static int defaultWidth = 500;
	private static int defaultHeight = 500;
	
	private int width;
	private int height;
	private Gate gate;
	
	public Meadow(){
		this.width = defaultWidth;
		this.height = defaultHeight;
		this.gate = new Gate();
		this.gate.setParentMeadow(this);
		this.setDefaultGatePos();
	}
	
	public Meadow(int width, int height, Gate gate){
		this.width = width;
		this.height = height;
		this.gate = gate;
	}
	
	public Meadow(int width, int height){
		this.width = width;
		this.height = height;
		
		this.gate = new Gate();
		this.gate.setParentMeadow(this);
		this.setDefaultGatePos();
	}
	
	public void setDefaultGatePos(){
		this.gate.move(this.width/2, this.gate.getHeight()/2);
	}
	
	public Gate getGate(){
		return this.gate;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public static void setDefaultWidth(int width){
		defaultWidth = width;
	}
	
	public static int getDefaultWidth(){
		return defaultWidth;
	}
	
	public static void setDefaultHeight(int height){
		defaultHeight = height;
	}
	
	public static int getDefaultHeight(){
		return defaultHeight;
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw meadow
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, 0, this.width, this.height);
		
		// Draw gate
		this.gate.draw(g);
	}
}
