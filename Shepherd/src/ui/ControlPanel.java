package ui;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

class ControlPanel extends JPanel{
	
	private GridBagLayout layout = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	private JPanel maxSpeedContainer;
	private JPanel speedSliderContainer;
	private JLabel speedSliderText;
	private JLabel maxSpeedText;
	private JSlider speedSlider;
	private JCheckBox maxSpeed;
	private JButton pause;
	
	
	ControlPanel(){
		this.setLayout(layout);
		
		/* Initialize instance variables */
		speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		maxSpeed = new JCheckBox();
		pause = new JButton("Pause simulation");
		speedSliderText = new JLabel("Simulation speed");
		maxSpeedText = new JLabel("Toggle maximum simulation speed: ");
		maxSpeedContainer = new JPanel();
		speedSliderContainer = new JPanel();
		
		/* Set focusable */
		speedSlider.setFocusable(false);
		maxSpeed.setFocusable(false);
		pause.setFocusable(false);
		
		/* Customization of components */
			// this
			this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			// this.maxSpeedContainer
			maxSpeedContainer.setLayout(new BoxLayout(maxSpeedContainer, BoxLayout.LINE_AXIS));
			maxSpeedContainer.add(maxSpeedText);
			maxSpeedContainer.add(maxSpeed);
			
			// this.speedSliderContainer
			speedSliderContainer.setLayout(new BoxLayout(speedSliderContainer, BoxLayout.PAGE_AXIS));
			speedSliderText.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			speedSliderContainer.add(speedSliderText);
			speedSliderContainer.add(speedSlider);
			
			// this.button
			Dimension pauseDimension = new Dimension(150, 25);
			this.pause.setPreferredSize(pauseDimension);
			
			
			// this.speedSlider
			/*
			   https://docs.oracle.com/javase/tutorial/uiswing/components/border.html
			   https://docs.oracle.com/javase/tutorial/uiswing/components/separator.html
			 */
			
			TitledBorder title = BorderFactory.createTitledBorder("Simulation speed settings");
			//speedSliderContainer.setBorder(title);
			// https://community.oracle.com/thread/1363951?start=0&tstart=0
			Dimension sliderDimension = new Dimension(500, 50);
			this.setDecimalLabels();
			this.speedSlider.setPreferredSize(sliderDimension);
			this.speedSlider.setMajorTickSpacing(1);
			//this.speedSlider.setMinorTickSpacing(1);
			this.speedSlider.setPaintTicks(true);
			this.speedSlider.setPaintLabels(true);
		
			
			// https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		/* Setting the layout */
		this.resetC();
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(pause, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		this.add(maxSpeedContainer, c);
		
		this.resetC();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		this.add(hSeparator(), c);
		
		this.resetC();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		this.add(speedSliderContainer, c);
		
		
		this.resetC();
		
	}
	
	JButton getPauseButton(){
		return this.pause;
	}
	
	JSlider getSlider(){
		return this.speedSlider;
	}
	
	JCheckBox getCheckBox(){
		return this.maxSpeed;
	}
	
	private void resetC(){
		this.c = new GridBagConstraints();
	}
	
	private JPanel hSeparator(){
		JPanel container = new JPanel();
		container.add(Box.createVerticalStrut(20));
		
		JSeparator s = new JSeparator(SwingConstants.HORIZONTAL);
		Dimension d = new Dimension(500, 1);
		s.setPreferredSize(d);
		container.add(s);
		
		container.add(Box.createVerticalStrut(20));
		return container;
	}
	
	// Þetta dót er nauðsynlegt því JSlider styður ekki tugabrotsgildi án bardaga.
	private void setDecimalLabels(){
		int ticks = this.speedSlider.getMaximum();
		Format f = new DecimalFormat("0.0");
        Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        for(int i=0;i<=ticks;i++){
        	if( i%10 == 0){
	            JLabel label = new JLabel(f.format(i*0.1));
	            label.setFont(label.getFont().deriveFont(Font.PLAIN));
	            labels.put(i,label);
        	}
        }
        this.speedSlider.setLabelTable(labels);
	}
}
