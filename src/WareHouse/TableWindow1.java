package WareHouse;


//	import Mainframe;
/*------------------------------------------------------------------------------------------------------------------*/
// TableWindow1 Class retrieves the data structure and renders both the Companies and Items tables.
// TableWindow1 also has 2 anonymous class containing  action listeners,called for when user selects an table item
// from Company or Item tables respectively.
// Selecting a row in the Company table has the effect of updating the Items Window. 
// Selecting a row in the Items table has the effect of updating the History table (class located in Table Window 2).
//
// Both the Companies and Items tables are non editable - risk of data inconsistency too high
/*-------------------------------------------------------------------------------------------------------------------*/
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.EventListenerList;
//import javax.swing.JTable.tableChanged;










import java.awt.BorderLayout;
import java.awt.Container;
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
import java.util.List;



public class TableWindow1 extends JPanel {
    private final InventoryController controller;
	private static final long serialVersionUID = 1L;
	private static final boolean FALSE = false;
	private static final boolean TRUE = false;
    private boolean DEBUG = false;
    public JTable table;
    public JTextField filterText;
    public JTextArea statusText;
    public TableRowSorter<MyTableModel> sorter;

    
 ///Table Panel 2
    public JTable table2;
    public JTextField filterText2;
    public JTextArea statusText2;
    public TableRowSorter<MyTableModel2> sorter2;
    public DetailsPanel detailsPanel;
    public final ArrayList<Item> items;
    public final ArrayList<Company> companies;
    public final ArrayList<history> history;
    public MyTableModel2[] model2 = new MyTableModel2[1];

    
 // Code for first 2 windows i.e. company and items window
    
    public TableWindow1(ArrayList<Item> items, ArrayList<Company> companies, ArrayList<history> history11, InventoryController controller)
    {
        super();
        this.controller = controller;
        setOpaque(true);
        setBackground(new java.awt.Color(240, 240, 255));
        this.history = history11;
        this.items = items;
        this.companies = companies;

        // These are used as workaround for non closures in java
        final MyTableModel2[] model2 = new MyTableModel2[1];
        final MyTableModel[] model = new MyTableModel[1];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        model[0] = new MyTableModel(this.companies);
        sorter = new TableRowSorter<MyTableModel>(model[0]);
        table = new JTable(model[0]) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Cancel the editing of any cell
            }
        };
        java.awt.Font itemTableFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
        table.setFont(itemTableFont);
        table.setRowHeight(24);
        table.setSelectionBackground(new java.awt.Color(220, 220, 255));
        table.setSelectionForeground(java.awt.Color.BLACK);
        table.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    int viewRow = table.getSelectedRow();
                    if (viewRow < 0) {
                        statusText.setText("");
                    } else {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        // Call controller method for company selection
                        try {
                            controller.getAllCompanies(); // Replace with actual method if needed
                        } catch (java.sql.SQLException ex) {
                            ex.printStackTrace();
                            statusText.setText("Error loading companies: " + ex.getMessage());
                        }
                        statusText.setText(String.format(" Company selected: %d.", modelRow));
                        // UI updates should be handled by controller callbacks or returned data
                    }
                }
            }
        );
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        JPanel form = new JPanel(new SpringLayout());
        JLabel l1 = new JLabel("Filter Text:", SwingConstants.TRAILING);
        l1.setPreferredSize(new Dimension(10,10));
        form.add(l1);
        filterText = new JTextField();
        //Whenever filterText changes, invoke newFilter.
        filterText.getDocument().addDocumentListener(
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
       
        l1.setLabelFor(filterText);
        form.add(filterText);
        JLabel l2 = new JLabel("Notes:", SwingConstants.TRAILING);
        l2.setPreferredSize(new Dimension(50,50));
        l2.setLabelFor(statusText);
        form.add(l2);
        statusText = new JTextArea("History for" ,1,4);
        statusText.setPreferredSize(new Dimension(50,50));
        statusText.setEditable(true); 
        JScrollPane scrollPane1 = new JScrollPane(statusText);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        form.add(l2);
        form.add(scrollPane1);
        SpringUtilites.makeCompactGrid(form, 2, 2, 6, 6, 6, 6);
    
        // --- FIX: Declare and initialize l12, l22, and form2 for the second filter/notes panel ---
        JPanel form2 = new JPanel(new SpringLayout());
        JLabel l12 = new JLabel("Filter Text:", SwingConstants.TRAILING);
        l12.setPreferredSize(new Dimension(10,10));
        filterText2 = new JTextField();
        JLabel l22 = new JLabel("Notes:", SwingConstants.TRAILING);
        l22.setPreferredSize(new Dimension(50,50));
        statusText2 = new JTextArea("History for" ,1,4);
        statusText2.setPreferredSize(new Dimension(50,50));
        statusText2.setEditable(true);
        JScrollPane scrollPane12 = new JScrollPane(statusText2);
        scrollPane12.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Add all 4 components in correct order
        form2.add(l12);
        form2.add(filterText2);
        form2.add(l22);
        form2.add(scrollPane12);

          // Only show items for the first company on startup
          int firstCompanyId = companies.get(0).getCompanyId();
          ArrayList<Item> firstCompanyItems = new ArrayList<>();
          for (Item item : items) {
              if (item.getCompanyId() == firstCompanyId) {
                  firstCompanyItems.add(item);
              }
          }
          model2[0] = new MyTableModel2(firstCompanyItems, 0);
          sorter2 = new TableRowSorter<MyTableModel2>(model2[0]);
          table2 = new JTable(model2[0]){
              public boolean isCellEditable(int rowIndex, int colIndex) {
                  return FALSE; //Disallow the editing of any cell
              }
          };
          table2.setRowSorter(sorter2);
          table2.setPreferredScrollableViewportSize(new Dimension(500, 200));
          table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          table2.getSelectionModel().addListSelectionListener(
              new ListSelectionListener() {
                  public void valueChanged(ListSelectionEvent event) {
                      if (event.getValueIsAdjusting()) {
                          return;
                      }
                      int viewRow = table2.getSelectedRow();
                      if (viewRow < 0) {
                          statusText2.setText("");
                      } else {
                          int modelRow = table2.convertRowIndexToModel(viewRow);
                          // Call controller method for item selection
                          // Example: controller.onItemSelected(modelRow);
                          // You may want to implement a method in InventoryController for this
                          statusText2.setText(String.format(" Item selected: %d.", modelRow));
                      }
                  }
              });
        // Set up filter and statusText2 listeners
        filterText2.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { newFilter2(); }
                public void insertUpdate(DocumentEvent e) { newFilter2(); }
                public void removeUpdate(DocumentEvent e) { newFilter2(); }
            });
        l12.setLabelFor(filterText2);
        l22.setLabelFor(statusText2);
        SpringUtilites.makeCompactGrid(form2, 2, 2, 6, 6, 6, 6);
        // Now that table2 is fully initialized, add it to the panel above the filter/notes
        JScrollPane scrollPane2 = new JScrollPane(table2);
        add(scrollPane2);
        add(form2);
    }

    /** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);	       
        
    }


    private void newFilter2() {
        RowFilter<MyTableModel2, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filterText2.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter2.setRowFilter(rf);
    }


    class MyTableModel extends AbstractTableModel {
    	private static final long serialVersionUID = 1L;
       private String[] columnNames = {
                                       "Company Name"
                                      };
    	
    /*	private String[] columnNames = {"Company Id",
    		     //                                   "Company Name",
    		                                       };*/
       private ArrayList<Company> company;
       private Object[][] data ;
       
       public MyTableModel(ArrayList<Company> company){
    	   this.company=company;
    	   int listSize = company.size();
    	   data = new Object[listSize][1];
    	   for(int i=0;i<listSize;i++){
    	//	   data[i][0]=(Object)company.get(i).getCompanyId();
    		   data[i][0]=(Object)company.get(i).getCompanyName();
    	   }
 
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

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            if (getRowCount() == 0) {
                return Object.class;
            }
            Object value = getValueAt(0, c);
            return (value == null) ? Object.class : value.getClass();
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
        
        public void updateModel(){
        	
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
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
    
    }
    
  
    public static class MyTableModel2 extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private String[] columnNames = {
            "Item Name",
            "Total(s)"
        };
        @SuppressWarnings("deprecation")

        private ArrayList<Item> items11;
        private Object[][] data;

        public MyTableModel2(ArrayList<Item> items11, int index1) {
            this.items11 = items11;
            int listSize = items11.size();
            data = new Object[listSize][2];
            for (int i = 0; i < listSize; i++) {
                data[i][0] = items11.get(i).getItemName();
                data[i][1] = items11.get(i).getQuantity();
            }
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

        public Class getColumnClass(int c) {
            if (getRowCount() == 0) {
                return Object.class;
            }
            Object value = getValueAt(0, c);
            return (value == null) ? Object.class : value.getClass();
        }

        public boolean isCellEditable(int row, int col) {
            // Only allow editing for the Total(s) column, if needed
            return col == 1;
        }

        public void updateModel(List<Item> items) {
            int listSize = items.size();
            data = new Object[listSize][2];
            for (int i = 0; i < listSize; i++) {
                data[i][0] = items.get(i).getItemName();
                data[i][1] = items.get(i).getQuantity();
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
    

    

    
    
}







