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
    private static final double mutationRate = 0.1;
    public static final int tournamentSize = 10;
    private static final boolean elitism = true;
    private static final int defaultElitismOffset = 1; // Það virðist hættulegt að stækka þ
    
    private static SimulationResult result;
    
    public static Population evolvePopulation(Population pop) {
    	MainLoop.currentGen++;
        Population newPopulation = new Population(pop.size(), false);
        
        int elitismOffset;
        if (elitism) {
            elitismOffset = defaultElitismOffset;
        } else {
            elitismOffset = 0;
        }

        if (elitism) {
        	for(int i = 0; i < elitismOffset; i++){
        		newPopulation.saveIndividual(i, pop.getFittest());
        	}
        }
        
        for (int i = 0; i < pop.size(); i++) {
        	MainLoop.currentIndiv = i+1;
        	testIndividual(pop.getIndividual(i));
        }
        
        pop.sort();
        
        for (int i = elitismOffset; i < pop.size(); i++) {
        	Shepherd indiv1 = tournamentSelection(pop);
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
    	Shepherd newSol = new Shepherd(100, 450);
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
    
    private static void testIndividual(Shepherd s){
    	SimulationResult result = MyMonitor.getResult(s);
    	s.setFitness(result);
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
        
        // Get the fittest
        Shepherd fittest = tournament.getFittest();
        return fittest;
    }

}
