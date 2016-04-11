package simulation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import utilities.MyPoint;
import utilities.Utils;

public class Creature extends Entity{
	
	protected static Meadow meadow;
	
	public double maxSpeed = 200;
	public double maxRotSpeed = Math.PI;
	
	protected boolean canMove = true;
	
	protected double rotSpeed = 0.0;
	protected double speed = 0.0;
	
	// Variables for drawing
	protected Color creatureColor = Color.BLACK;
	protected int size = 10;
	protected int noseSize = 15;
	
	public Creature(){
		super();
	}
	
	public Creature(MyPoint p){
		super(p);
	}
	
	public Creature(double x, double y){
		super(x, y);
	}
	
	public Creature RandCreature(){
		double x = Math.random()*Meadow.getDefaultWidth();
		double y = Math.random()*Meadow.getDefaultHeight();
		MyPoint p = new MyPoint(x, y);
		return new Creature(p);
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public void setRotSpeed(double rot){
		this.rotSpeed = rot;
	}
	
	public void update(double du){
		if(!canMove){return;}
		
		MyPoint nextLoc = this.loc.clone();
		nextLoc.translatePolar(this.speed*du, this.rotation);
		
		this.rotation += du*this.rotSpeed;
		
		if(Utils.isInside(nextLoc, 0, 0, meadow.getWidth(), meadow.getHeight()))
			this.translatePolar(this.speed*du, this.rotation);
		
	}
	
	public void translateCart(double x, double y){
		this.loc.translateCart(x, y);
	}
	
	public void translateCart(MyPoint p){
		this.loc.translateCart(p);
	}
	
	public void translatePolar(double r, double t){
		this.loc.translatePolar(r, t);
	}
	
	public void setCanMove(boolean p){
		this.canMove = p;
	}
	

	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(creatureColor);
		int x = (int) Math.round(this.loc.getX());
		int y = (int) Math.round(this.loc.getY());
		g2d.fillOval(x-this.size, y-this.size, 2*this.size, 2*this.size);
		
		int xPointer = x + (int) Math.round(noseSize*Math.cos(this.rotation));
		int yPointer = y + (int) Math.round(noseSize*Math.sin(this.rotation));
		g2d.drawLine(x, y, xPointer, yPointer);
	}
	
	public static void setMeadow(Meadow m){
		meadow = m;
	}
}
