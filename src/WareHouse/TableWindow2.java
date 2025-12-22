package WareHouse;


/*------------------------------------------------------------------------------------------------------------------*/
//TableWindow2 Class retrieves the data structure (from memory) and renders the History tables.
//TableWindow1 also has 1 anonymous class containing  action listeners,called for when user selects an table item
//from History tables respectively.
// History tables is non editable - risk of data inconsistency too high
//Selecting a row in the History table has the effect of updating the data entry controls (located in the DetailsPanel Class). 
//
// Most Companies purchase manufacturers items from the same suppliers usually this will ease the data entry procedure
/*-------------------------------------------------------------------------------------------------------------------*/

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.User;
import WareHouse.domain.history;

import WareHouse.controller.InventoryController;

public class TableWindow2 extends JPanel {
    private ArrayList<history> history11,history;
    private ArrayList<Company> companies11;
    private ArrayList<Item> items11,item;

    private static final long serialVersionUID = 1L;
    private static final boolean FALSE = false;
    private static final boolean TRUE = false;
    private boolean DEBUG = false;
    public static JTable table3;
    private JTextField filterText3;
    private JTextArea statusText3;
    public TableRowSorter<MyTableModel> sorter3;
    public int nmrRowsTable3;
    public MyTableModel model;
    private final InventoryController controller;

    public TableWindow2(ArrayList<Item> items, ArrayList<Company> companies, ArrayList<history> history11, InventoryController controller) {
        super();
        setOpaque(true);
        setBackground(new java.awt.Color(255, 240, 240));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setPreferredSize(new java.awt.Dimension(400, 300));
        this.history11 = history11;
        this.items11 = items;
        this.companies11 = companies;
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        model = new MyTableModel(companies.get(0).getItems().get(0).getHistory(),0);
        sorter3 = new TableRowSorter<MyTableModel>(model);
        table3 = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        java.awt.Font tableFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
        table3.setFont(tableFont);
        table3.setRowHeight(24);
        table3.setSelectionBackground(new java.awt.Color(255, 220, 220));
        table3.setSelectionForeground(java.awt.Color.BLACK);
        table3.setRowSorter(sorter3);
        table3.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table3.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        if (event.getValueIsAdjusting()) {
                            return;
                        }
                        int viewRow = table3.getSelectedRow();
                        if (viewRow < 0 || viewRow >= table3.getRowCount()) {
                            statusText3.setText("");
                            return;
                        }
                        int modelRow = table3.convertRowIndexToModel(viewRow);
                        // Call controller method for history selection
                        // Example: controller.onHistorySelected(modelRow);
                        statusText3.setText(String.format("History row selected: %d", modelRow));
                    }
                }
        );
        
        JScrollPane scrollPane3 = new JScrollPane(table3);
        add(scrollPane3);       
        JPanel form3 = new JPanel(new SpringLayout());
        JLabel l13 = new JLabel("Filter Text:", SwingConstants.TRAILING);
        l13.setPreferredSize(new Dimension(10,10));
        form3.add(l13);
        filterText3 = new JTextField();
        //Whenever filterText changes, invoke newFilter.
        filterText3.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
       
        l13.setLabelFor(filterText3);
        form3.add(filterText3);
        JLabel l23 = new JLabel("Notes:", SwingConstants.TRAILING);
        l23.setPreferredSize(new Dimension(50,50));
        l23.setLabelFor(statusText3);
        form3.add(l23);
        statusText3 = new JTextArea("History for" ,1,4);
       statusText3.setPreferredSize(new Dimension(50,50));
       statusText3.setEditable(true); 
       JScrollPane scrollPane12 = new JScrollPane(statusText3);
       scrollPane12.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        form3.add(l23);
        form3.add(scrollPane12);
        SpringUtilites.makeCompactGrid(form3, 2, 2, 6, 6, 6, 6);
        add(form3);    
    }

    /** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
        	rf = RowFilter.regexFilter(filterText3.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter3.setRowFilter(rf);
    }




    public static class MyTableModel extends AbstractTableModel {
    	private static final long serialVersionUID = 1L;
        private String[] columnNames = {
                        "Delivery Date",
                        "Location",
                        "Quantity",
                        "Supplier"};
        private ArrayList<history> history11;
        
        @SuppressWarnings("deprecation")
		private Object[][] data;
        
            public MyTableModel(ArrayList<history> history,int index1){
                this.history11=history;
                int index=index1;
                int listSize = history11.size();
                data = new Object[listSize][4];
                for(int i = 0; i < listSize; i++)
                {
                    data[i][0]=(Object)history11.get(i).getDeliveryDate();
                    data[i][1]=(Object)history11.get(i).getLocation();
                    data[i][2]=(Object)history11.get(i).getAmount();
                    data[i][3]=(Object)history11.get(i).getSupplier();
                }
            }
        
        
	
	
       
        public Object[][] convertTo2D() {
        	
        	int listSize = history11.size();
        	final Object[][] data2 = new Object[listSize][4];
        
        	for(int i = 0; i < listSize; i++)
        	{
       // 		data2[0][i]=(Object)history11.get(i).getHistoryId();
       // 		data2[i][1]=(Object)history11.get(i).getItemId();
        		data2[i][0]=(Object)history11.get(i).getDeliveryDate();
                data2[i][1]=(Object)history11.get(i).getLocation();
        		data2[i][2]=(Object)history11.get(i).getAmount();
        		data2[i][3]=(Object)history11.get(i).getSupplier();
        	}
			return data2;
        }
  
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

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
       public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
       public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (data == null || data.length == 0) return;
            if (row < 0 || row >= data.length) return;
            if (data[0] == null) return;
            // Defensive: check all rows for null and correct length
            if (col < 0) return;
            if (row >= data.length) return;
            if (data[row] == null || col >= data[row].length) return;
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

       private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
        
       public void set(){
    	   
       }
    }
}

