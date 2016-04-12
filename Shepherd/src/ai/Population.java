package ai;
// http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3
//http://stackoverflow.com/questions/75175/create-instance-of-generic-type-in-java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import simulation.Shepherd;

public class Population {
	private Shepherd[] individuals;
	
	// Constructors
	public Population(int populationSize, boolean initialize){
		individuals = new Shepherd[populationSize];
		if (initialize) {
			for(int i = 0; i < populationSize; i++){
				Shepherd newIndividual = new Shepherd(100, 450);
				newIndividual.generateShepherd();
				saveIndividual(i, newIndividual);
			}
		}
	}
	
    // Getters
	public Shepherd getIndividual(int n){
		return individuals[n];
	}
	
	public void sort(){
		Arrays.sort(this.individuals);
	}
	
	public Shepherd getFittest(){
		this.sort();
		return individuals[0];
	}
	
	public int size(){
		return individuals.length;
	}
	
	public void saveIndividual(int index, Shepherd individual){
		individuals[index] = individual.clone();
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int i = 0; i < this.individuals.length; i++){
			s += this.individuals[i].getPosition().toString();
			s += "\n";
		}
		return s;
	}
}
