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
	private ArrayList<Cell> cells = new ArrayList<Cell>(); // Sjá innri klasann Cell.
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
	
	// Það ætti basically aldrei að þurfa að nota þetta. Þetta er bara hér til gamans.
	void setText(String name, Object text){
		Cell cell = cells.get(map.get(name));
		cell.setText(text);
	}
	
	// Það ætti basically aldrei að þurfa að nota þetta. Þetta er bara hér til gamans.
	void setName(String name, String newName){
		Cell cell = cells.get(map.get(name));
		cell.setName(newName);
	}
	
	private class Cell extends JPanel{
		private JLabel text = new JLabel(); // Skýringartexti með gögnum
		private JLabel data = new JLabel(); // Gögn
		private GridLayout layout = new GridLayout(1, 2);
		
		/* 
		  Það er í lagi að nota box layout, en það lætur gögnin í hverju instance'i
		  birtast beint á eftir textanum í því, þ.e. gögnin verða sett fram eins og
		  bæði gögnin, text og data, séu í sama reit frekar en að vera í tveimur
		  mismunandi reitum af fastri stærð.
		  Það má lesa lýsinguna á BoxLayout og GridLayout til að skilja þetta betur
		  ef þessi texti er ekki skýr.
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
