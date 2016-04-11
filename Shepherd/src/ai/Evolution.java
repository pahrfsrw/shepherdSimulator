package ai;
import java.util.concurrent.CountDownLatch;

import simulation.MainLoop;
import simulation.Shepherd;
import simulation.SimulationResult;
import threadHelpher.MyMonitor;
import ui.MainWindow;

public class Evolution {
	
	private static boolean isWaiting = false;
	private static CountDownLatch latch = new CountDownLatch(1);
	private static MyMonitor monitor = MainLoop.monitor;
	private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    public static final int tournamentSize = 5;
    private static final boolean elitism = true;
    
    private static SimulationResult result;
    
    public static Population evolvePopulation(Population pop) {
    	MainLoop.currentGen++;
        Population newPopulation = new Population(pop.size(), false);

        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        
        for (int i = elitismOffset; i < pop.size(); i++) {
        	//System.out.println("Breeding for indiv " + i );
        	MainLoop.currentIndiv = i;
        	MainLoop.currentTournament = 1;
            Shepherd indiv1 = tournamentSelection(pop);
            //System.out.println("Selecting indiv 2");
            MainLoop.currentTournament = 2;
            Shepherd indiv2 = tournamentSelection(pop);
            Shepherd newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Shepherd crossover(Shepherd indiv1, Shepherd indiv2) {
    	Shepherd newSol = new Shepherd();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Shepherd indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    // Select individuals for crossover
    private static Shepherd tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        
        // Reiknum út fitness hjá hverjum einstaklingi
        for(int i = 0; i < tournamentSize; i++){
        	//System.out.println("Tournament place: " + i);
        	MainLoop.currentTournamentRound = i+1;
    		SimulationResult result = MyMonitor.getResult(tournament.getIndividual(i));
        	tournament.getIndividual(i).setFitness(result);
        }
        
        // Get the fittest
        Shepherd fittest = tournament.getFittest();
        return fittest;
    }
    
    // http://stackoverflow.com/questions/289434/how-to-make-a-java-thread-wait-for-another-threads-output

}
