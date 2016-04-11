package simulation;

import java.awt.Color;
import java.io.Serializable;

import utilities.MyPoint;
import utilities.Utils;

public class Sheep extends Creature{	
	
	private double flightDuration = 2.0;
	private double flightDistance = 80;
	private double fenceAvoidDistance = 30;
	
	private double flightDistanceSq = flightDistance*flightDistance;
	
	private double currentFlightTime = 0.0;
	
	public Sheep(){
		super();
		creatureColor = Color.WHITE;
		size = 5;
		noseSize = 6;
		maxSpeed = 100.0;
		maxRotSpeed = Math.PI*4;
	}
	
	public Sheep(MyPoint p){
		super(p);
		creatureColor = Color.WHITE;
		size = 5;
		noseSize = 6;
		maxSpeed = 100.0;
		maxRotSpeed = Math.PI*4;
	}
	
	public Sheep(double x, double y){
		super(x, y);
		creatureColor = Color.WHITE;
		size = 5;
		noseSize = 6;
		maxSpeed = 100.0;
		maxRotSpeed = Math.PI*4;
	}
	
	public boolean shouldIFlee(Creature c){
		if(Creature.distanceSq(this, c) < this.flightDistanceSq && this.canMove){
			this.initFlight();
			return true;
		}
		return false;
	}
	
	private void initFlight(){
		this.currentFlightTime = this.flightDuration;
		this.speed = this.maxSpeed;
	}
	
	public void flee(Creature c, Meadow m, double dt){
		boolean p = shouldIFlee(c);
		
		if(this.currentFlightTime <= 0.0){
			this.speed = 0.0;
			this.rotSpeed = 0.0;
			return;
		}
		this.currentFlightTime -= dt;
		
		// ------------------------------------
		// L�tum kindurnar beg�ast vi� gir�ingunum
		// ------------------------------------
		MyPoint point = this.distanceFromFence();
		if(Math.abs(point.getX()) < this.fenceAvoidDistance || Math.abs(point.getY()) < this.fenceAvoidDistance){
			this.pushBack();
		} else {
			// ------------------------------------
			// Ef kindurnar breg�ast ekki vi� gir�ingu l�tum vi� kindurnar beg�ast vi� smalanum
			// ------------------------------------
			if(p){
				// �ttin sem kindin �arf a� sn�a � til �ess a� komast sem hra�ast burt fr� smalanum,
				// �.e. sama horn og kindin �yrfti a� sn�a � ef h�n vildi fara beinustu lei� � �tt a� smalanum.
				double rotationGoal = MyPoint.angle(c.getPosition(), this.loc);
				
				double rotationDifference = rotationGoal-this.rotation;
				while(rotationDifference < Math.PI){
					rotationDifference += 2*Math.PI;
				}
				while(rotationDifference > Math.PI){
					rotationDifference -= 2*Math.PI;
				}

				if(Utils.isZero(rotationDifference, Math.PI/6)){
					this.rotSpeed = 0;
				} else {
					if (rotationDifference > 0)
						this.rotSpeed = this.maxRotSpeed;
					else if (rotationDifference <= 0)
						this.rotSpeed = -this.maxRotSpeed;
				}
			}
			else{
				this.rotSpeed = 0;
			}
		}
		
		this.update(dt);
		Gate g = m.getGate();
		if(isInside(g) && g.getSticky() == true){
			this.canMove = false;
		}
	}
	
	public MyPoint distanceFromFence(){
		double x = this.loc.getX() < meadow.getWidth()/2 ? -this.loc.getX() : meadow.getWidth() - this.loc.getX();
		double y = this.loc.getY() < meadow.getHeight()/2 ? -this.loc.getY() : meadow.getHeight() - this.loc.getY();
		
		return new MyPoint(x, y);
	}
	
	public void pushBack(){
		// �arf a� laga �a� a� l�ta 250 bara vera t�frat�lu sem er mi�jan � sv��inu.
		MyPoint t = new MyPoint(250-this.loc.getX(), 250-this.loc.getY());
		
		double rotationGoal = t.getT();
		double rotationDifference = rotationGoal-this.rotation;
		
		while(rotationDifference < Math.PI){
			rotationDifference += 2*Math.PI;
		}
		while(rotationDifference > Math.PI){
			rotationDifference -= 2*Math.PI;
		}
		
		// --------------------------
		// Point p = this.distanceFromFence();
		// �g vil a� sn�nings�ttin s� t�knu� me� vigri.
		// Lengdin � vigrinum er 1/(fjarl�g� a� gir�ingu)
		// double theta = Math.atan(Math.max(p.getX(), p.getY())/Math.min(p.getX(), p.getY()));
		// double r = 1/Math.min(p.getX(), p.getY());
		// Stefnan � vigrinum er erfi�...

		if(Utils.isZero(rotationDifference, Math.PI/6)){
			this.rotSpeed = 0;
		} else {
			if (rotationDifference > 0)
				this.rotSpeed = this.maxRotSpeed/2;
			else if (rotationDifference <= 0)
				this.rotSpeed = -this.maxRotSpeed/2;
		}
	}
	
	
	public boolean isInside(Gate g){
		return g.hasInside(this);
	}
	
	
	
}
