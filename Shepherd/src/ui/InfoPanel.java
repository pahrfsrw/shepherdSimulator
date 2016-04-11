package ui;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InfoPanel extends JPanel {
	
	private HashMap<String, Integer> map = new HashMap();
	
	private GridLayout layout = new GridLayout(0, 2);
	private ArrayList<Cell> cells = new ArrayList<Cell>(); // Sj� innri klasann Cell.
	private int panelSize = 0;
	
	InfoPanel(){
		this.setLayout(layout);
	}
	
	void addCell(Object name){
		Cell cell = new Cell();
		map.put(name.toString(), panelSize++);
		cells.add(cell);
		this.add(cell);
	}
	
	void addCell(Object name, Object text){
		addCell(name);
		Cell cell = cells.get(map.get(name));
		cell.setText(text);
	}
	
	void addCell(Object name, Object text, Object data){
		addCell(name, text);
		Cell cell = cells.get(map.get(name));
		cell.setData(data);
	}
	
	public void setData(String name, Object data){
		Cell cell = cells.get(map.get(name));
		cell.setData(data);
	}
	
	// �a� �tti basically aldrei a� �urfa a� nota �etta. �etta er bara h�r til gamans.
	void setText(String name, Object text){
		Cell cell = cells.get(map.get(name));
		cell.setText(text);
	}
	
	// �a� �tti basically aldrei a� �urfa a� nota �etta. �etta er bara h�r til gamans.
	void setName(String name, String newName){
		Cell cell = cells.get(map.get(name));
		cell.setName(newName);
	}
	
	private class Cell extends JPanel{
		private JLabel text = new JLabel(); // Sk�ringartexti me� g�gnum
		private JLabel data = new JLabel(); // G�gn
		private GridLayout layout = new GridLayout(1, 2);
		
		/* 
		  �a� er � lagi a� nota box layout, en �a� l�tur g�gnin � hverju instance'i
		  birtast beint � eftir textanum � �v�, �.e. g�gnin ver�a sett fram eins og
		  b��i g�gnin, text og data, s�u � sama reit frekar en a� vera � tveimur
		  mismunandi reitum af fastri st�r�.
		  �a� m� lesa l�singuna � BoxLayout og GridLayout til a� skilja �etta betur
		  ef �essi texti er ekki sk�r.
		*/
		//private BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		
		public Cell(){
			this.setLayout(layout);
			this.add(this.text);
			this.add(this.data);
		}
		
		public void setText(Object text){
			this.text.setText(text.toString());
		}
		
		public void setData(Object data){
			this.data.setText(data.toString());
		}
	}
	
}
