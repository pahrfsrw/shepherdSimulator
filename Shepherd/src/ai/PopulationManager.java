package ai;

import simulation.Shepherd;

public class PopulationManager {
	
	private static Population pop = new Population(50, true);
	
	public static Population getPopulation(){
		return pop;
	}
	
	public static void setPopulation(Population newPop){
		pop = newPop;
	}
	
	public static Object[][] getPopInfo(){
		Object[][] data = new Object[pop.size()][5];
		for(int i = 0; i < pop.size(); i++){
			Shepherd s = pop.getIndividual(i);
			data[i][0] = new Integer(i);
			data[i][1] = "n/a";
			data[i][2] = "n/a";
			data[i][3] = s.getPosition().getX();
			data[i][4] = s.getPosition().getY();
		}
		return data;
	}
	
	public static Object[] getPopInfo(int rowIndex){
		Object[] data = new Object[5];
			Shepherd s = pop.getIndividual(rowIndex);
			data[0] = new Integer(rowIndex);
			data[1] = "n/a";
			data[2] = "n/a";
			data[3] = s.getPosition().getX();
			data[4] = s.getPosition().getY();
		return data;
	}
	
	public static void printPop(){
		System.out.println("Population from PopulationManager (prints only ten): ");
		for(int i = 0; i < 10; i++){
			System.out.println(pop.getIndividual(i).getPosition());
		}
	}
}
