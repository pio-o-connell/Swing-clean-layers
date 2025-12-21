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
import java.util.List;



public class TableWindow1 extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final boolean FALSE = false;
	private static final boolean TRUE = false;
    private boolean DEBUG = false;
    private JTable table;
    private JTextField filterText;
    private JTextArea statusText;
    private TableRowSorter<MyTableModel> sorter;

    
 ///Table Panel 2
	private JTable table2;
	private JTextField filterText2;
	private JTextArea statusText2;
	private TableRowSorter<MyTableModel2> sorter2;
	private DetailsPanel detailsPanel;
    private final ArrayList<Item> items;
    private final ArrayList<Company> companies;
    private final ArrayList<history> history;

    
 // Code for first 2 windows i.e. company and items window
    
    public TableWindow1(ArrayList<Item> items,ArrayList<Company> companies,ArrayList<history> history11)
    {
        super();
        setOpaque(true);
        setBackground(new java.awt.Color(240, 240, 255));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setPreferredSize(new java.awt.Dimension(400, 300));
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
        table.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    int viewRow = table.getSelectedRow();
                    if (viewRow < 0) {
                        statusText.setText("");
                    } else {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        Mainframe.companyIndex = modelRow;
                        statusText.setText(String.format(" Company selected: %s   Selected Row in model: %d.",
                                maindriver.Company11.get(modelRow).getCompanyName(), modelRow));
                        // Update current company field in DetailsPanel
                        DetailsPanel.currentCompanyField.setText(maindriver.Company11.get(modelRow).getCompanyName());
                        // Use the selected company's item list directly
                        ArrayList<Item> filteredItems = new ArrayList<>(maindriver.Company11.get(modelRow).getItems());
                        model2[0] = new MyTableModel2(filteredItems, 0);
                        table2.setModel(model2[0]);
                        sorter2.setModel(model2[0]);
                        // Select the first item if available
                        if (!filteredItems.isEmpty()) {
                            table2.setRowSelectionInterval(0, 0);
                            Item firstItem = filteredItems.get(0);
                            String itemName = firstItem.getItemName();
                            java.util.List<history> histories = firstItem.getHistory();
                            if (histories != null && !histories.isEmpty()) {
                                history firstHistory = histories.get(0);
                                String supplier = firstHistory.getSupplier();
                                String location = firstHistory.getLocation();
                                String delivery = firstHistory.getDeliveryDate();
                                int amount = firstHistory.getAmount();
                                DetailsPanel.nameField.setText(itemName);
                                DetailsPanel.locationField.setText(location);
                                DetailsPanel.supplierField.setText(supplier);
                                DetailsPanel.deliveryField.setText(delivery);
                                DetailsPanel.amountField.setText(String.valueOf(amount));
                            } else {
                                DetailsPanel.nameField.setText(itemName);
                                DetailsPanel.locationField.setText("");
                                DetailsPanel.supplierField.setText("");
                                DetailsPanel.deliveryField.setText("");
                                DetailsPanel.amountField.setText(String.valueOf(firstItem.getQuantity()));
                            }
                        }
                    }
                }
            }
        );
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
            // ...existing code...
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
     //  took this out  add(form);
    
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
                          //Selection got filtered away.
                          statusText2.setText("");
                      } else {
                          int modelRow = table2.convertRowIndexToModel(viewRow);
                          if (modelRow < 0 || modelRow >= maindriver.Company11.get(Mainframe.companyIndex).getItems().size()) {
                              statusText2.setText("");
                              return;
                          }
                          Mainframe.itemIndex = modelRow;
                          statusText2.setText(
                              String.format(" Item selected: %s. " +
                                  "Selected Row in model: %d.",
                                  maindriver.Company11.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getItemName(), modelRow));

                          // clear table 3
                          int rowsToClear = TableWindow2.model.getRowCount();
                          int colsToClear = TableWindow2.model.getColumnCount();
                          for (int i = 0; i < rowsToClear; i++) {
                              for (int j = 0; j < colsToClear; j++) {
                                  TableWindow2.model.setValueAt(" ", i, j);
                              }
                          }

                          ArrayList<history> currentHistoryPointer = maindriver.Company11.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getHistory();
                          int listSize = currentHistoryPointer.size();
                          for (int i = 0; i < listSize; i++) {
                              // Write directly to the model (view indices can be invalid when sorting/filtering).
                              TableWindow2.model.setValueAt((Object) currentHistoryPointer.get(i).getDeliveryDate(), i, 0);
                              TableWindow2.model.setValueAt((Object) currentHistoryPointer.get(i).getLocation(), i, 1);
                              TableWindow2.model.setValueAt((Object) currentHistoryPointer.get(i).getAmount(), i, 2);
                              TableWindow2.model.setValueAt((Object) currentHistoryPointer.get(i).getSupplier(), i, 3);
                          }

                          // Update DetailsPanel with the most recent history record if available
                          if (listSize > 0) {
                              history latestHistory = currentHistoryPointer.get(listSize - 1); // Use the most recent (last) history record
                              DetailsPanel.nameField.setText(maindriver.Company11.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getItemName());
                              DetailsPanel.locationField.setText(latestHistory.getLocation());
                              DetailsPanel.deliveryField.setText(latestHistory.getDeliveryDate());
                              DetailsPanel.amountField.setText(String.valueOf(latestHistory.getAmount()));
                              DetailsPanel.supplierField.setText(latestHistory.getSupplier());
                              DetailsPanel.reportDeliveryFrom.setText(latestHistory.getDeliveryDate());
                              DetailsPanel.reportDeliveryTo.setText(latestHistory.getDeliveryDate());
                              DetailsPanel.notesArea.setText(latestHistory.getNotes());
                          } else {
                              // If no history, fill with item info
                              Item selectedItem = maindriver.Company11.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex);
                              DetailsPanel.nameField.setText(selectedItem.getItemName());
                              DetailsPanel.locationField.setText("");
                              DetailsPanel.deliveryField.setText("");
                              DetailsPanel.amountField.setText(String.valueOf(selectedItem.getQuantity()));
                              DetailsPanel.supplierField.setText("");
                          }

                          TableWindow2.nmrRowsTable3 = listSize;
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
    
  
    class MyTableModel2 extends AbstractTableModel {
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

        public void updateModel() {
            final ArrayList<Item> tableItemPointer = maindriver.Company11.get(Mainframe.companyIndex).getItems();
            int listSize = tableItemPointer.size();
            data = new Object[listSize][2];
            for (int i = 0; i < listSize; i++) {
                data[i][0] = tableItemPointer.get(i).getItemName();
                data[i][1] = tableItemPointer.get(i).getQuantity();
            }
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
    

    

    
    
}



	    
	    
	

