package ui;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DebuggerTable extends JPanel{
	
		private JTable table;
		private JScrollPane scrollPane;
		private DefaultTableModel model;
		private MyTableModel model1;
		
		private String[] columnNames = {"Individual", "Sheep herded", "Best time", "x-position", "y-position"};
		private Object[][] defaultData = {
				{"Default entry 1", new Integer(0), new Integer(2000), new Integer(10), new Integer(20)},
				{"Default entry 2", new Integer(3), new Integer(1000), new Integer(30), new Integer(30)}
		};
		
		public DebuggerTable(){
			model = new DefaultTableModel(defaultData, columnNames);
			table = new JTable(model);
			//table.getModel().addTableModelListener(this);
			table.setFillsViewportHeight(true);
			scrollPane = new JScrollPane(table);
			
			this.add(scrollPane);
		}
		
		public void updateData(int rowIndex, Object[] data){
			for(int i = 0; i < data.length; i++){
				this.model.setValueAt(data[i], rowIndex, i);
			}
			//model.setvalue
			//model.fireTableRowsUpdated(rowIndex, rowIndex);
			
			//this.table.setModel(this.model);
			////this.model.fireTableDataChanged();
			////this.table.setValueAt
		}
		
		public void resetData(Object[][] data){
			this.model = new DefaultTableModel(data, this.columnNames);
			this.table.setModel(this.model);
			////this.model.fireTableDataChanged();
			////this.table.setValueAt
		}
		
	
		public void tableChanged(TableModelEvent e) {
	        //int row = e.getFirstRow();
	        //int column = e.getColumn();
	        //TableModel model = (TableModel)e.getSource();
	        //String columnName = model.getColumnName(column);
	        //Object data = model.getValueAt(row, column);

	        //this.table.getModel().setValueAt(data, row, column);
	    }
		
		private class MyTableModel extends AbstractTableModel{
			private String[] columnNames = {"Individual", "Sheep herded", "Best time", "x-position", "y-position"};
			private Object[][] data = {
					{"Default entry 1", new Integer(0), new Integer(2000), new Integer(10), new Integer(20)},
					{"Default entry 2", new Integer(3), new Integer(1000), new Integer(30), new Integer(30)}
			};
			
			public int getColumnCount() {
		        return columnNames.length;
		    }

		    public int getRowCount() {
		        return data.length;
		    }

		    public String getColumnName(int col) {
		        return columnNames[col];
		    }

		    public Object getValueAt(int row, int col) {
		        return data[row][col];
		    }
		    
		    public boolean isCellEditable(int row, int col) {
		        if (col < 2) {
		            return false;
		        } else {
		            return true;
		        }
		    }
		    
		    public void setValueAt(Object value, int row, int col) {
		        data[row][col] = value;
		        fireTableCellUpdated(row, col);
		    }	        
		}
		
}
