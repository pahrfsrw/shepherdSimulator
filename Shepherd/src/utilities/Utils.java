package utilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.CountDownLatch;

public class Utils {
	public static final double NANO = 1000000000.0;
	
			
	public static MyPoint[] initPoints(int n, int m, MyPoint a, MyPoint b, double density){
		MyPoint[] points = new MyPoint[n*m];
		double w = Math.abs(a.getX()-b.getX())/density;
		double h = Math.abs(a.getY()-b.getY())/density;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				points[i*n+j] = new MyPoint(a.getX() + i*w, a.getY() + j*h);
			}
		}
		return points;
	}
	
	public static MyPoint[] initPointsRand(int n, MyPoint a, MyPoint b){
		MyPoint points[] = new MyPoint[n];
		for(int i = 0; i < n; i++){
			double x = Math.random()*Math.abs(a.getX()-b.getX())+a.getX();
			double y = Math.random()*Math.abs(a.getY()-b.getY())+a.getY();
			points[i] = new MyPoint(x, y);
		}
		return points;
	}
	
	public static boolean isBetween(double x, double a, double b){
		return (a < x && x < b);
	}
	
	public static boolean isInside(MyPoint p, double x1, double y1, double x2, double y2){
		double x = p.getX();
		double y = p.getY();
		
		return (isBetween(x, x1, x2) && isBetween(y, y1, y2));
	}
	
	public static double sign(double x){
		if(x < 0)
			return -1.0;
		return 1.0;
	}
	
	public static boolean isZero(double x){
		return Math.abs(x) < 0.0000001;
	}
	
	public static boolean isZero(double x, double accuracy){
		return Math.abs(x) < accuracy;
	}
	
	public static <E> E coinFlip(E heads, E tails){
		if(isZero(Math.round(Math.random())))
				return heads;
		return tails;
	}
	
	public static boolean differentSigns(double a, double b){
		if(a*b < 0) 
			return true;
		return false;
	}
	
}
