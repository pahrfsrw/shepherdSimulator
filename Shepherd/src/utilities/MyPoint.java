package utilities;

public class MyPoint {
	private double x = 0;
	private double y = 0;
	private static double distanceToleranceSq = 0.25;
	
	public MyPoint(){}
	
	public MyPoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public static MyPoint PointPolar(double r, double t){
		double x = r*Math.cos(r);
		double y = r*Math.sin(r);
		return new MyPoint(x, y);
	}
	
	public void scale(double s){
		this.x *=s;
		this.y *=s;
	}
	
	public void moveCart(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void moveCart(MyPoint p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public void movePolar(double r, double t){
		this.x = r*Math.cos(t);
		this.y = r*Math.sin(t);
	}
	
	public void translateCart(double x, double y){
		this.x += x;
		this.y += y;
	}
	
	public void translateCart(MyPoint p){
		this.x += p.x;
		this.y += p.y;
	}
	
	public void translatePolar(double r, double t){
		this.x += r*Math.cos(t);
		this.y += r*Math.sin(t);
	}
	
	public double distance(double x, double y){
		double x1 = Math.pow(this.x-x, 2);
		double y1 = Math.pow(this.y-y, 2);
		return Math.sqrt(x1+y1);
	}
	
	public double distance(MyPoint p){
		double x = Math.pow(this.x-p.x, 2);
		double y = Math.pow(this.y-p.y, 2);
		return Math.sqrt(x+y);
	}
	
	public static double distance(double x1, double y1, double x2, double y2){
		double x = Math.pow(x1-x2, 2);
		double y = Math.pow(y1-y2, 2);
		return Math.sqrt(x+y);
	}
	
	public static double distance(MyPoint p1, MyPoint p2){
		double x = Math.pow(p1.x-p2.x, 2);
		double y = Math.pow(p1.y-p2.y, 2);
		return Math.sqrt(x+y);
	}
	
	public double distanceSq(double x, double y){
		double X = Math.pow(this.x-x, 2);
		double Y = Math.pow(this.y-y, 2);
		return X+Y;
	}
	
	public double distanceSq(MyPoint p){
		double x = Math.pow(this.x-p.x, 2);
		double y = Math.pow(this.y-p.y, 2);
		return x+y;
	}
	
	public static double distanceSq(double x1, double y1, double x2, double y2){
		double x = Math.pow(x1-x2, 2);
		double y = Math.pow(y1-y2, 2);
		return x+y;
	}
	
	public static double distanceSq(MyPoint p1, MyPoint p2){
		double x = Math.pow(p1.x-p2.x, 2);
		double y = Math.pow(p1.y-p2.y, 2);
		return x+y;
	}
	
	public double length(){
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public void toUnitLength(){
		
		double l = this.length();
		this.x /= l;
		this.y /= l;
		
	}
	
	public double angle(double x, double y){
		return Math.atan2(y-this.y, x-this.x);
	}
	
	public double angle(MyPoint p){
		return Math.atan2(p.y-this.y, p.x-this.x);
	}
	
	public static double angle(double x1, double y1, double x2, double y2){
		return Math.atan2(y2-y1, x2-x1);
	}
	
	public static double angle(MyPoint p1, MyPoint p2){
		return Math.atan2(p2.y-p1.y, p2.x-p1.x);
	}
	
	public boolean equals(MyPoint p){
		return this.distanceSq(p) < distanceToleranceSq ? true : false;
	}
	
	public static boolean equals(MyPoint p1, MyPoint p2){
		return distanceSq(p1, p2) < distanceToleranceSq ? true : false;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getR(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public double getT(){
		return Math.atan2(this.y, this.x);
	}
	
	@Override
	public MyPoint clone(){
		return new MyPoint(this.x, this.y);
	}

	@Override
	public String toString(){
		return "X : " + this.x + ", Y : " + this.y + ".";
	}
}
