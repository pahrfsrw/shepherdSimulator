package simulation;

public class SimulationResult implements Comparable<SimulationResult>{
	/* Ra�a� eftir mikilv�gi ni�urst��u */
	public final int sheepHerded; 					// Fj�ldi kinda sem var b�i� a� smala �egar t�minn kl�ra�ist.
	public final int time;	  					// T�minn sem �a� t�k smalann a� smala s��ustu kindinni sem t�kst a� smala ��ur en t�minn kl�ra�ist.
	public final double avgHerdDistance;			// Me�alfjarl�g� hjar�arinnar fr� hli�inu �egar t�minn kl�ra�ist.
	public final double herdDensity;				// Me�alfjarl�g� hjar�arinnar fr� eigin massami�ju.
	public final boolean hasHerdMoved;			 	// �a� �arf a� hvetja smalann til a� reyna a� hreyfa kindurnar.
	public final double distanceToClosestSheep; 	// �a� �arf a� hvetja smalann til a� n�lgast kindurnar.
	public final boolean hasShepherdMoved; 			// �a� �arf a� hvetja smalann til a� hreyfa sig.
	
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
