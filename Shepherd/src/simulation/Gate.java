package simulation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Gate extends Entity{
		
		private static int defaultWidth = 100;
		private static int defaultHeight = 50;
		private static boolean defaultSticky = true;
		
		private int width = defaultWidth;
		private int height = defaultHeight;
		private boolean sticky = defaultSticky;
		
		private Meadow parentMeadow;
		
		public Gate(){super();}
		
		public Gate(int width, int height, boolean sticky){
			this.width = width;
			this.height = height;
			this.sticky = sticky;
		}
		
		public Gate(int width, int height){
			this.width = width;
			this.height = height;
		}
		
		public int getWidth(){
			return this.width;
		}
		
		public int getHeight(){
			return this.height;
		}
		
		public boolean getSticky(){
			return this.sticky;
		}
		
		public void setParentMeadow(Meadow m){
			this.parentMeadow = m;
		}
		
		public static double getDefaultHeight(){
			return defaultHeight;
		}
		
		public static void setDefaultHeight(int height){
			defaultHeight = height;
		}
		
		public static double getDefaultWidth(){
			return defaultWidth;
		}
		
		public static void setDefaultWidth(int width){
			defaultWidth = width;
		}
		
		public static boolean getDefaultSticky(){
			return defaultSticky;
		}
		
		public static void setDefaultSticky(boolean sticky){
			defaultSticky = sticky;
		}
		
		public boolean hasInside(Entity e){
			double x = e.getPosition().getX();
			double y = e.getPosition().getY();
			double leftX = this.loc.getX() - this.width/2;
			double rightX = this.loc.getX() + this.width/2;
			double topY = this.loc.getY() - this.height/2;
			double bottomY = this.loc.getY() + this.height/2;
			
			if(leftX < x && x < rightX)
				if(topY < y && y < bottomY)
					return true;
			return false;
		}
		
		public void draw(Graphics g){
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(3));
			int x = (int) Math.round(this.loc.getX());
			int y = (int) Math.round(this.loc.getY());
			int[] xPoints = {x-this.width/2, x+this.width/2, x+this.width/2, x-this.width/2};
			int[] yPoints = {y-this.height/2, y-this.height/2, y+this.height/2, y+this.height/2};
			
			g2d.drawPolygon(xPoints, yPoints, 4);
		}
	}