package simulation;

public class SimulationResult implements Comparable<SimulationResult>{
	/* Raðað eftir mikilvægi niðurstöðu */
	public final int sheepHerded; 					// Fjöldi kinda sem var búið að smala þegar tíminn kláraðist.
	public final int time;	  					// Tíminn sem það tók smalann að smala síðustu kindinni sem tókst að smala áður en tíminn kláraðist.
	public final double avgHerdDistance;			// Meðalfjarlægð hjarðarinnar frá hliðinu þegar tíminn kláraðist.
	public final double herdDensity;				// Meðalfjarlægð hjarðarinnar frá eigin massamiðju.
	public final boolean hasHerdMoved;			 	// Það þarf að hvetja smalann til að reyna að hreyfa kindurnar.
	public final double distanceToClosestSheep; 	// Það þarf að hvetja smalann til að nálgast kindurnar.
	public final boolean hasShepherdMoved; 			// Það þarf að hvetja smalann til að hreyfa sig.
	
	private static int timeBenefit = 500;
	private static double hDistBenefit = 5;
	private static double hDensBenefit = 5;
	private static double dtcsBenefit = 5;
	
	public SimulationResult(int sheepHerded, 
							int time, 
							double avgHerdDistance,
							double herdDensity,
							boolean hasHerdMoved, 
							double distanceToClosestSheep, 
							boolean hasShepherdMoved)
	{
		this.sheepHerded = sheepHerded;
		this.time = time;
		this.avgHerdDistance = avgHerdDistance;
		this.herdDensity = herdDensity;
		this.hasHerdMoved = hasHerdMoved;
		this.distanceToClosestSheep = distanceToClosestSheep;
		this.hasShepherdMoved = hasShepherdMoved;
	}
	
	public static SimulationResult getWorst(){
		return new SimulationResult(0, Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, false, Double.MAX_VALUE, false);
	}
	
	
	public int compareTo(SimulationResult res){
		if(this.sheepHerded > res.sheepHerded) return 1;
		else if(this.sheepHerded < res.sheepHerded) return -1;
		else{
			if(this.time - timeBenefit > res.time) return 1;
			else if(res.time - timeBenefit > this.time) return -1;
			else{
				if(this.avgHerdDistance - hDistBenefit > res.avgHerdDistance) return 1;
				else if (res.avgHerdDistance - hDistBenefit> this.avgHerdDistance) return -1;
				else{
					if(this.herdDensity - hDensBenefit > res.herdDensity) return 1;
					else if (res.avgHerdDistance - hDensBenefit > this.herdDensity) return -1;
					else{
						if(this.hasHerdMoved == true && res.hasHerdMoved == false) return 1;
						else if (res.hasHerdMoved == true && this.hasHerdMoved == false) return -1;
						else{
							if(this.distanceToClosestSheep - dtcsBenefit > res.distanceToClosestSheep) return 1;
							else if (res.distanceToClosestSheep - dtcsBenefit > this.distanceToClosestSheep) return -1;
							else{
								if(this.hasShepherdMoved == true && res.hasShepherdMoved == false) return 1;
								else if (res.hasShepherdMoved == true && this.hasShepherdMoved == false) return -1;
								else{
									return 0;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public SimulationResult clone(){
		return new SimulationResult(
					this.sheepHerded,
					this.time,
					this.avgHerdDistance,
					this.herdDensity,
					this.hasHerdMoved,
					this.distanceToClosestSheep,
					this.hasShepherdMoved
				);
	}
}
