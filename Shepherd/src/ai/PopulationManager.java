package ai;

public class PopulationManager {
	
	private static Population pop = new Population(50, true);
	
	public static Population getPopulation(){
		return pop;
	}
	
	public static void printPop(){
		System.out.println("Population from PopulationManager (prints only ten): ");
		for(int i = 0; i < 10; i++){
			System.out.println(pop.getIndividual(i).getPosition());
		}
	}
}
