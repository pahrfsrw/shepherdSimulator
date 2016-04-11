package ui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulation.EntityManager;
import simulation.MainLoop;
import simulation.Shepherd;
import simulation.SimulationResult;
import utilities.Utils;

import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainWindow implements KeyListener, MouseMotionListener, ActionListener{

	// // http://www.java-gaming.org/index.php?topic=24220.0
	
	
	// Components
	// Components have been indented to show their internal structure.
	private JFrame frame;
		private ControlPanel controlPanel;
			private JButton pauseButton;
			private JSlider speedSlider;
			private JCheckBox maxSpeed;
		private JMenuBar mb;
			private JMenu mb_file;
				private JMenuItem mb_file_newSimulation;
				private JMenuItem mb_file_saveSimulation;
				private JMenuItem mb_file_loadSimulation;
				private JMenu mb_file_import;
					private JMenuItem mb_file_import_herd;
					private JMenuItem mb_file_import_shepherd;
				private JMenu mb_file_export;
					private JMenuItem mb_file_export_herd;
					private JMenuItem mb_file_export_shepherd;
				private JMenuItem mb_file_exit;
		private DrawingAreaUnderlay dau;
			private DrawingArea da;
		public InfoPanel infoPanel;

	public MainWindow() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		
		// frame dót
		frame = new JFrame("Shepherding simulator");
		
		// Látum nýja hluti sem bætt er við frame raðast lóðrétt niður ("PAGE_AXIS"). "LINE_AXIS" mundi láta allt raðast lárétt.
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		// Skiptir ekki máli hverjar seinni tvær tölurnar eru, því aðrir hlutir verða stærri en þetta. Fyrri tvær tölurnar eru staðsetning forritsins á skjánum.
		frame.setBounds(100, 100, 200, 200); 
		
		//frame.setPreferredSize(new Dimension(1000, 1000)); // Ekki setja preferred size á frame. Þá virkar frame.pack ekki fyrir undirhluti ef frame.prefSize er stærra.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addMenuBar();
		addButtonArea();
		addGameArea();
		addInfoPanel();
		
		frame.pack();
		
		frame.requestFocusInWindow();
		
		setClosingBehavior();
		addListeners();
		
	}
	
	private void addMenuBar(){
		
		// Initialization
		mb = new JMenuBar();
		mb_file = new JMenu("File");
		mb_file_import = new JMenu("Import");
		mb_file_export = new JMenu("Export");
		mb_file_exit = new JMenuItem("Exit");
	    mb_file_import_herd = new JMenuItem("Import herd...");
	    mb_file_export_herd = new JMenuItem("Export herd...");
	    mb_file_import_shepherd = new JMenuItem("Import shepherd...");
	    mb_file_export_shepherd = new JMenuItem("Export shepherd...");
	    mb_file_newSimulation = new JMenuItem("New simulation");
	    mb_file_saveSimulation = new JMenuItem("Load simulation");
	    mb_file_loadSimulation = new JMenuItem("Load simulation");
	    
		// Composition
		mb_file_import.add(mb_file_import_herd);
		mb_file_import.add(mb_file_import_shepherd);
		mb_file_export.add(mb_file_export_herd);
		mb_file_export.add(mb_file_export_shepherd);
		mb_file.add(mb_file_newSimulation);
		mb_file.add(mb_file_saveSimulation);
		mb_file.add(mb_file_loadSimulation);
		mb_file.addSeparator();
		mb_file.add(mb_file_import);
		mb_file.add(mb_file_export);
		mb_file.addSeparator();
		mb_file.add(mb_file_exit);
		mb.add(mb_file);
		
		// Layout
		mb_file_import.setPreferredSize(new Dimension(100, 20)); // Other menus and items automatically adjust to same size.
		
		// Add listeners to buttons
		ExitAction ea = new ExitAction();
		//ea.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X")); // Lætur ctrl+x vera exit shortcut. Hef ekki prófað þetta.
		mb_file_exit.addActionListener(ea);
		
		
		// Sniðugt?: http://stackoverflow.com/questions/8707392/java-exit-button-from-a-menuitem-menubar-and-sound-player
		//menuBar_menu_exit.addActionListener(...);
		
		// Finally
		frame.setJMenuBar(mb);
	}
	
	private void addButtonArea(){
		controlPanel = new ControlPanel();
		frame.getContentPane().add(controlPanel);
		pauseButton = controlPanel.getPauseButton();
		speedSlider = controlPanel.getSlider();
		maxSpeed = controlPanel.getCheckBox();
	}
	
	private void addGameArea(){
		// Þetta er smá svæði allan hringinn utan um leiksvæðið.
		dau = new DrawingAreaUnderlay();
		
		// Þetta er leiksvæðið sjálft, þ.e. þar sem allt sem gerist í leiknum er teiknað.
		da = new DrawingArea();
		
		// Þetta lætur dau vera 50px stærra en da á báða kanta. Svo er da fært niður og til hægri um 25 = 50/2px.
		dau.setPreferredSize(new Dimension(550, 550));
		da.setPreferredSize(new Dimension(500, 500));
		da.setBounds(25, 25, 500, 500);
		
		frame.getContentPane().add(dau);
		dau.add(da);
		
		da.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}
	
	
	private void addInfoPanel(){
		// Bjó til nýjan klasa InfoPanel sem lætur það vera mjög auðvelt og þægilegt
		// að sýna hvaða gögn sem maður vill OG gerir það mjög auðvelt að sýna uppfærslur
		// á öllum gögnum.
		infoPanel = new InfoPanel();
		frame.getContentPane().add(infoPanel);
		infoPanel.addCell("leftCol", "Current generation info:");
		infoPanel.addCell("rightCol", "General info:");
		infoPanel.addCell("gen", "Current generation: ", "n/a");
		infoPanel.addCell("sim", "Simulation: ", "n/a");
		infoPanel.addCell("genTime", "Current gen. time: ", "n/a");
		infoPanel.addCell("totalTime", "Total time: ", "n/a");
		infoPanel.addCell("genFrames", "Current gen in-game time: ", "n/a");
		infoPanel.addCell("totalFrames", "Total in-game time: ", "n/a");
		infoPanel.addCell("sheep", "Sheep herded: ", "n/a");
		infoPanel.addCell("bestTime", "Best time: ", "n/a");
		infoPanel.addCell("herdSize", "Herd size:", EntityManager.getInstance().getHerdSize());
		infoPanel.addCell("critTime", "Critical in-game time:", MainLoop.criticalTime);
	}
	
	private void addListeners(){
		frame.addKeyListener(this);
		frame.addMouseMotionListener(this);
		pauseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
	        {
				MainLoop.isPaused = !MainLoop.isPaused;
	            if(MainLoop.isPaused){
	            	pauseButton.setText("Resume simulation");
	            	MainLoop.pauseTime = System.nanoTime() - MainLoop.pauseTime;
	            }
	            else{
	            	pauseButton.setText("Pause simulation");
	            	MainLoop.pauseTime = System.nanoTime() - MainLoop.pauseTime;
	            }
	        }
		});
		
		speedSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e)
	        {
				JSlider source = (JSlider)e.getSource();
				MainLoop.gameSpeed = source.getValue();
	        }
		});
		
		maxSpeed.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e)
	        {
				MainLoop.atMaxGameSpeed = !MainLoop.atMaxGameSpeed;
				MainLoop.doRender = !MainLoop.doRender;
				MainLoop.doInfoUpdates = !MainLoop.doInfoUpdates;
				speedSlider.setEnabled(!MainLoop.atMaxGameSpeed);
				if(MainLoop.atMaxGameSpeed)
					MainLoop.gameSpeed = MainLoop.maxGameSpeed;
				else
					MainLoop.gameSpeed = speedSlider.getValue();
				
	        }
		});
		
	}
	
	private void setClosingBehavior(){
		// https://tips4java.wordpress.com/2009/05/01/closing-an-application/
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener( new WindowAdapter()
		{
			@Override
		    public void windowClosing(WindowEvent e)
		    {
		        JFrame frame = (JFrame)e.getSource();
		 
		        int result = JOptionPane.showConfirmDialog(
		            frame,
		            "Are you sure you want to exit the application?",
		            "Exit Application",
		            JOptionPane.YES_NO_OPTION);
		 
		        if (result == JOptionPane.YES_OPTION)
		            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    }
		});
	}
	
	public void render(){
		da.repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		Shepherd shepherd = EntityManager.getInstance().getShepherd();
		
		if(e.getKeyCode()== KeyEvent.VK_RIGHT)
            shepherd.setRotSpeed(shepherd.maxRotSpeed);
        if(e.getKeyCode()== KeyEvent.VK_LEFT)
        	shepherd.setRotSpeed(-shepherd.maxRotSpeed);
        if(e.getKeyCode()== KeyEvent.VK_DOWN)
        	shepherd.setSpeed(-shepherd.maxSpeed);
        if(e.getKeyCode()== KeyEvent.VK_UP)
        	shepherd.setSpeed(shepherd.maxSpeed);
    }
	
	public void keyTyped(KeyEvent e) {
	       
	}

	public void keyReleased(KeyEvent e) {
		Shepherd shepherd = EntityManager.getInstance().getShepherd();
		
		if(e.getKeyCode()== KeyEvent.VK_RIGHT)
            shepherd.setRotSpeed(0.0);
        if(e.getKeyCode()== KeyEvent.VK_LEFT)
        	shepherd.setRotSpeed(0.0);
        if(e.getKeyCode()== KeyEvent.VK_DOWN)
        	shepherd.setSpeed(0.0);
        if(e.getKeyCode()== KeyEvent.VK_UP)
        	shepherd.setSpeed(0.0);
	}
	
	public void mouseMoved(MouseEvent e){
		//mouseX = e.getX();
		//mouseY = e.getY();
		//System.out.println("x: " + mouseX + " y: " + mouseY);
	}
	
	public void mouseDragged(MouseEvent e){

	}
	
	public void actionPerformed(ActionEvent e){
		JMenuItem source = (JMenuItem)(e.getSource());
		//if (source == menuBar_menu_exit){
		//	System.exit(0);
		//}
	}
}
