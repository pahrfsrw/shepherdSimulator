package simulation;

import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

import ai.Evolution;
import ai.Population;
import ai.PopulationManager;
import threadHelpher.MyMonitor;
import ui.MainWindow;
import utilities.MyPoint;
import utilities.Utils;

public class MainLoop {
	
	public static MainWindow window;
	
	// Variables and constants
	public static boolean isRunning = true;
	public static boolean isPaused = false;
	public static boolean hasWon = false;
	public static boolean doRender = true;
	public static boolean doInfoUpdates = true;
	public static int gameSpeed = 10;
	public static boolean atMaxGameSpeed = false;
	public static int maxGameSpeed = 500000000;
	public static int fps = 60;
	
	
	// Time keeping stuff
	public static DecimalFormat df = new DecimalFormat("0.0"); // Ekki "#.#" því "#" þýðir "sýndu tölustafinn í þessu sæti nema hann sé 0",
	   													 // t.d. mundi "5.0" bara sjást sem "5". Hins vegar þýðir "0" "sýndu alltaf þennan tölustaf".
	public static double runTime = 0;
	public static long currentSimTime = 0;
	public static long currentSimStartTime = 0;
	public static long pauseTime = 0;
	public static long startTime = 0;
	public static int currentSimFrameCount = 0;
	public static int totalFrameCount = 0;
	
	
	// Misc. simulation stuff:
	public static int currentGen = 0;
	public static int currentIndiv = 0;
	public static int currentTournament = 0;
	public static int currentTournamentRound = 0;
	public static int allTimeMostSheepHerded = 0;
	public static int allTimeBestTime = Integer.MAX_VALUE;
	
	public static int criticalTime = 149040; // Sirka 240 sekúndur á venjulegum hermishraða.
	public static int criticalGen = (int) Math.pow(10, 6);
	
	// Simulation data
	
		public static int framesBetweenSnapshots = 120;
		public static int numberOfSnapshots = 0;
		public static int lastSnapshotFrame = 0;
		// Þetta er notað til þess að reikna út meðalfjarlægð allrar hjarðarinnar
		// frá hliðinu gegnum heila hermun. Fjarlægðin er bara reiknuð á
		// nokkurra ramma fresti (framesBetweenSnapshots) til að auka hraða forritsins.
		// Þannig að meðaltal í lok hermunar er avg = sumHerdDistance/numberOfSnapshots.
		public static double sumHerdDistance = 0;
		
		// Þetta virkar eins og meðalfjarlægðin.
		public static double sumHerdDensity = 0;
		
		//
		public static double leastDistanceToAnySheep = Double.MAX_VALUE;
		
		// Minnstu tölurnar yfir alla hermunina. Það er spurning hvort maður vilji nota þetta.
		public static final boolean calculateLeastDistance = true;
		public static double smallestHerdDistance = Double.MAX_VALUE;
		public static double smallestHerdDensity = Double.MAX_VALUE;
	
	private static void runEvolutionLoop(){
		Thread evolution = new Thread("Evolution thread")
		{
			public void run(){
				evolutionLoop();
			}
		};
		evolution.start();
		
	}
	
	private static void runGameLoop(){
	      Thread loop = new Thread("Game loop thread")
	      {
	         public void run()
	         {
	            simulationLoop();
	         }
	      };
	      loop.start();
	}
	
	private static void simulationLoop(){
		//EntityManager.getInstance().setShepherd(MyMonitor.init());
		
		// Þetta er til þess að geta teiknað gluggann (window) rétt í upphafi og það komi ekki nullpointer exception
		// þegar EntityManager reynir að teikna Shepherd.
		Shepherd tempPlaceholder = new Shepherd();
		EntityManager.getInstance().setShepherd(tempPlaceholder);
		window = new MainWindow();
		
	    
	    // Notað til að fylgjast með hvað forritið er búið að keyra lengi.
	    startTime = System.nanoTime();
	    currentSimStartTime = System.nanoTime();
	    
	    while (isRunning && currentGen < criticalGen){
	    	
	    	window.debuggerWindow.resetPopulationData(PopulationManager.getPopInfo());
	    	Population pop = PopulationManager.getPopulation();
	    	//System.out.println();
	    	//System.out.println("From mainLoop");
	    	//System.out.println(pop);
	    	//pop = Evolution.evolvePopulation(pop);
	    	PopulationManager.setPopulation(Evolution.evolvePopulation(PopulationManager.getPopulation()));
	    	
	    	
	    }
	}
	
	public static SimulationResult gameLoop(Shepherd s){
		newSim(s);
		
		final int TARGET_FPS = 60;
		final long TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;
		long nextTime = System.nanoTime();
		
		SimulationResult result = null;
	    
		while (currentSimFrameCount < criticalTime){
			
			long currentTime = System.nanoTime();
			runTime = currentTime - startTime;
			
			if(!isPaused && !hasWon){
				currentSimTime = System.nanoTime() - currentSimStartTime - pauseTime;
				long updateLength = currentSimTime - nextTime;
				nextTime = currentSimTime;
				double toSeconds = (double)TIME_BETWEEN_UPDATES*TARGET_FPS;
				double delta = updateLength / toSeconds;
				delta *= gameSpeed;
				
				
				// Ekki gera þau mistök að margfalda einfaldlega delta með 10.
				// Collision miðast við hversu langt einhver vera er að fara að hreyfa sig á næsta tímaskrefi delta.
				// Ef að delta er t.d. tífaldað þá þýðir það að vera hreyfir sig tífalt lengra í hverju skrefi
				// og því fer collision detection í gang allt of snemma.
				for(int i = 0; i < gameSpeed; i++){
					updateSim(delta/(gameSpeed*10));
					currentSimFrameCount++;
					totalFrameCount++;
					updateSimulationData();
					if(hasWon || currentSimFrameCount > criticalTime){
						result = win();
						break;
					}
				}
				
				if(doRender){
					window.render();
				}
				
				try{
					Thread.sleep( (nextTime-currentSimTime + TIME_BETWEEN_UPDATES)/1000000 );
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			
			if(doInfoUpdates){
				updateInfo();
				updateDebuggerInfo();
			}
			
			
			
		}
		
		return result;
	}
	
	private static void updateSim(double du){	
		// Update simulation
		EntityManager simulation = EntityManager.getInstance();
		simulation.update(du);
		hasWon = simulation.winCondition();
	}
	
	private static SimulationResult win(){
		EntityManager em = EntityManager.getInstance();
		SimulationResult result = 
				new SimulationResult(em.getHerded(),
									 currentSimFrameCount,
									 sumHerdDistance/(double)numberOfSnapshots,
									 sumHerdDensity/(double)numberOfSnapshots,
									 em.getHasHerdMoved(),
									 leastDistanceToAnySheep,
									 em.getHasShepherdMoved()
									 );
		if(em.getHerded() > allTimeMostSheepHerded){
			allTimeMostSheepHerded = em.getHerded();
		}
		
		if(currentSimFrameCount < allTimeBestTime){
			allTimeBestTime = currentSimFrameCount;
		}
		
		//em.getShepherd().move(100, 450);
		//newSim();
		//EntityManager.getInstance().setShepherd(MyMonitor.produceResult(result));
		hasWon = false;
		return result;
	}
	
	private static void updateSimulationData(){
		if(currentSimFrameCount - lastSnapshotFrame < framesBetweenSnapshots){
			lastSnapshotFrame = currentSimFrameCount;
			EntityManager em = EntityManager.getInstance();
			double currentHerdDistance = em.getHerdDistance();
			double currentHerdDensity = em.getHerdDensity();
			double smallestDistanceToSheep = em.getDistanceToClosestSheep();
			sumHerdDistance += currentHerdDistance;
			sumHerdDensity += currentHerdDensity;
			if(smallestDistanceToSheep < leastDistanceToAnySheep){
				leastDistanceToAnySheep = smallestDistanceToSheep;
			}
			
			numberOfSnapshots++;
			
			if(calculateLeastDistance){
				if(smallestHerdDistance > currentHerdDistance)
					smallestHerdDistance = currentHerdDistance;
				if(smallestHerdDensity > currentHerdDensity)
					smallestHerdDensity = currentHerdDensity;
			}
		}
	}
	
	private static void updateInfo(){
		// Update data in info panel
		window.infoPanel.setData("totalTime", 	df.format(runTime/Utils.NANO));
		window.infoPanel.setData("sheep", EntityManager.getInstance().getHerded());
		window.infoPanel.setData("genTime", df.format(currentSimTime/Utils.NANO));
		window.infoPanel.setData("gen", currentGen);
		window.infoPanel.setData("genFrames", currentSimFrameCount);
		window.infoPanel.setData("totalFrames", totalFrameCount);
		window.infoPanel.setData("genIndiv", currentIndiv);
		window.infoPanel.setData("bestTime", allTimeBestTime);
		window.infoPanel.setData("mostSheepHerded", allTimeMostSheepHerded);
	}
	
	public static void updateDebuggerInfo(){
		if(!window.debuggerWindow.isVisible()) return;
		Shepherd s = EntityManager.getInstance().getShepherd();
		MyPoint p = s.loc;
		int x = (int) Math.round(p.getX());
		int y = (int) Math.round(p.getY());
		window.debuggerWindow.setData("shepCoord", "X: " + x + " Y: " + y);
		Object[] o = {new Integer(currentIndiv-1), "n/a", "n/a", new Double(s.loc.getX()), new Double(s.loc.getY())};
		window.debuggerWindow.editPopulationData(currentIndiv-1, o);
	}
	
	private static void newSim(Shepherd s){
		currentSimStartTime = System.nanoTime();
		currentSimFrameCount = 0;
		EntityManager.getInstance().newSim(s);
	}
	
	private static void evolutionLoop(){
		Population pop;
		while(true){
			System.out.println("Pre");
			PopulationManager.printPop();
			pop = PopulationManager.getPopulation();
			pop = Evolution.evolvePopulation(pop);
			System.out.println("Post");
			PopulationManager.printPop();
		}
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					runGameLoop();
					//runEvolutionLoop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
