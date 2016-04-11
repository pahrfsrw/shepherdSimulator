package simulation;
import utilities.MyPoint;

public class Entity {
	protected MyPoint loc;
	protected double rotation;
	
	public Entity(){
		this.loc = new MyPoint();
	}
	
	public Entity(MyPoint loc){
		this.loc = loc.clone();
	}
	
	public Entity(double x, double y){
		this.loc = new MyPoint(x, y);
	}
	
	public void move(double x, double y){
		this.loc.moveCart(x, y);
	}
	
	public void move(MyPoint p){
		this.loc.moveCart(p);
	}
	
	public void movePolar(double r, double t){
		this.loc.movePolar(r, t);
	}
	
	public MyPoint getPosition(){
		return this.loc;
	}
	
	public double getRotation(){
		return this.rotation;
	}
	
	public void setRotation(double theta){
		this.rotation = theta;
	}
	
	public double distance(Entity e){
		return this.loc.distance(e.loc);
	}
	
	public static double distance(Entity e1, Entity e2){
		return MyPoint.distance(e1.loc, e2.loc);
	}
	
	public double distanceSq(Entity e){
		return this.loc.distanceSq(e.loc);
	}
	
	public static double distanceSq(Entity e1, Entity e2){
		return MyPoint.distanceSq(e1.loc, e2.loc);
	}
}
