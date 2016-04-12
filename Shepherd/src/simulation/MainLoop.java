package simulation;

import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

import ai.Evolution;
import ai.Population;
import threadHelpher.MyMonitor;
import ui.MainWindow;
import utilities.MyPoint;
import utilities.Utils;

public class MainLoop {
	
	private final Object lock = new Object();
	public static MyMonitor monitor = new MyMonitor();
	
	public static MainWindow window;
	private static boolean isWaiting = false;
	private static CountDownLatch latch = new CountDownLatch(1);
	
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
	public static DecimalFormat df = new DecimalFormat("0.0"); // Ekki "#.#" �v� "#" ���ir "s�ndu t�lustafinn � �essu s�ti nema hann s� 0",
	   													 // t.d. mundi "5.0" bara sj�st sem "5". Hins vegar ���ir "0" "s�ndu alltaf �ennan t�lustaf".
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
	
	public static int criticalTime = 149040; // Sirka 240 sek�ndur � venjulegum hermishra�a.
	public static int criticalGen = (int) Math.pow(10, 6);
	
	// Simulation data
	
		public static int framesBetweenSnapshots = 120;
		public static int numberOfSnapshots = 0;
		public static int lastSnapshotFrame = 0;
		// �etta er nota� til �ess a� reikna �t me�alfjarl�g� allrar hjar�arinnar
		// fr� hli�inu gegnum heila hermun. Fjarl�g�in er bara reiknu� �
		// nokkurra ramma fresti (framesBetweenSnapshots) til a� auka hra�a forritsins.
		// �annig a� me�altal � lok hermunar er avg = sumHerdDistance/numberOfSnapshots.
		public static double sumHerdDistance = 0;
		
		// �etta virkar eins og me�alfjarl�g�in.
		public static double sumHerdDensity = 0;
		
		// Minnstu t�lurnar yfir alla hermunina. �a� er spurning hvort ma�ur vilji nota �etta.
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
	            gameLoop();
	         }
	      };
	      loop.start();
	}
	
	private static void gameLoop(){
		MyMonitor.produceResult(null);
		
		final int TARGET_FPS = 60;
		
		final long TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;

		long nextTime = System.nanoTime();
	    
	    // Nota� til a� fylgjast me� hva� forriti� er b�i� a� keyra lengi.
	    startTime = System.nanoTime();
	    currentSimStartTime = System.nanoTime();
	    
		while (isRunning && currentGen < criticalGen){
			
			long currentTime = System.nanoTime();
			runTime = currentTime - startTime;
			
			if(!isPaused && !hasWon){
				currentSimTime = System.nanoTime() - currentSimStartTime - pauseTime;
				long updateLength = currentSimTime - nextTime;
				nextTime = currentSimTime;
				double toSeconds = (double)TIME_BETWEEN_UPDATES*TARGET_FPS;
				double delta = updateLength / toSeconds;
				delta *= gameSpeed;
				
				
				// Ekki gera �au mist�k a� margfalda einfaldlega delta me� 10.
				// Collision mi�ast vi� hversu langt einhver vera er a� fara a� hreyfa sig � n�sta t�maskrefi delta.
				// Ef a� delta er t.d. t�falda� �� ���ir �a� a� vera hreyfir sig t�falt lengra � hverju skrefi
				// og �v� fer collision detection � gang allt of snemma.
				for(int i = 0; i < gameSpeed; i++){
					updateSim(delta/(gameSpeed*10));
					currentSimFrameCount++;
					totalFrameCount++;
					updateSimulationData();
					if(hasWon || currentSimFrameCount > criticalTime){
						win();
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
			};
			
			if(doInfoUpdates){
				updateInfo();
				updateDebuggerInfo();
			}
			
			
			// F�r�i �etta h��an. Ef �etta var h�r kom index out of bounds ef hra�inn var aukinn of miki�.
			/*if(hasWon || currentSimFrameCount > criticalTime){
				win();
			}*/
			
		}
		
	}
	
	private static void updateSim(double du){	
		// Update simulation
		EntityManager simulation = EntityManager.getInstance();
		simulation.update(du);
		hasWon = simulation.winCondition();
	}
	
	private static void win(){
		EntityManager em = EntityManager.getInstance();
		SimulationResult result = 
				new SimulationResult(em.getHerded(),
									 currentSimFrameCount,
									 smallestHerdDistance/(double)numberOfSnapshots,
									 smallestHerdDensity/(double)numberOfSnapshots,
									 em.getHasHerdMoved(),
									 em.getDistanceToClosestSheep(),
									 em.getHasShepherdMoved()
									 );
		if(em.getHerded() > allTimeMostSheepHerded){
			allTimeMostSheepHerded = em.getHerded();
		}
		
		if(currentSimFrameCount < allTimeBestTime){
			allTimeBestTime = currentSimFrameCount;
		}
		EntityManager.getInstance().setShepherd(MyMonitor.produceResult(result));

		newSim();
	}
	
	private static void updateSimulationData(){
		if(currentSimFrameCount - lastSnapshotFrame < framesBetweenSnapshots){
			lastSnapshotFrame = currentSimFrameCount;
			EntityManager em = EntityManager.getInstance();
			double currentHerdDistance = em.getHerdDistance();
			double currentHerdDensity = em.getHerdDensity();
			sumHerdDistance += currentHerdDistance;
			sumHerdDensity += currentHerdDensity;
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
		MyPoint p = EntityManager.getInstance().getShepherd().loc;
		int x = (int) Math.round(p.getX());
		int y = (int) Math.round(p.getY());
		window.debuggerWindow.setData("shepCoord", "X: " + x + " Y: " + y);
	}
	
	private static void newSim(){
		currentSimStartTime = System.nanoTime();
		currentSimFrameCount = 0;
		EntityManager.getInstance().newSim();
		hasWon = false;
	}
	
	private static void evolutionLoop(){
		Population pop;
		while(true){
			System.out.println("Pre");
			EntityManager.getInstance().printPop();
			pop = EntityManager.getInstance().getPopulation();
			pop = Evolution.evolvePopulation(pop);
			System.out.println("Post");
			EntityManager.getInstance().printPop();
		}
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainWindow();
					runGameLoop();
					runEvolutionLoop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
