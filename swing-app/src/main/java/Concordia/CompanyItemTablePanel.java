package concordia;

//	import Mainframe;
/*------------------------------------------------------------------------------------------------------------------*/
// CompanyItemTablePanel Class retrieves the data structure and renders both the Companies and Items tables.
// CompanyItemTablePanel also has 2 anonymous class containing  action listeners,called for when user selects an table item
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
import java.util.List;
import java.util.Date;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;

import concordia.controller.InventoryController;
import java.util.List;

public class CompanyItemTablePanel extends JPanel {
    private final InventoryController controller;
    private static final long serialVersionUID = 1L;
    private static final boolean FALSE = false;
    private boolean DEBUG = false;
    public JTable companyTable;
    public JTextField companyFilterText;
    public JTextArea companyStatusText;
    public TableRowSorter<CompanyModel> companyNameSorter;

    /// Table Panel 2
    public JTable itemTable;
    public JTextField itemFilterText;
    public JTextArea itemStatusText;
    public TableRowSorter<ItemModel> itemNameSorter;
    public final List<Item> items;
    public final List<Company> companies;
    public final List<History> history;
    public ItemModel[] itemModelRefreshRef = new ItemModel[1];
    public CompanyModel[] companyModelRefreshRef = new CompanyModel[1];

    // Code for first 2 windows i.e. company and items window

        public CompanyItemTablePanel(List<Item> items, List<Company> companies, List<History> history,
            InventoryController controller) {
        super();
        this.controller = controller;
        setOpaque(true);
        setBackground(new java.awt.Color(240, 240, 255));
        this.history = history;
        this.items = items;
        this.companies = companies;

        // These are used as workaround for non closures in java
        final ItemModel[] itemModel = new ItemModel[1];
        final CompanyModel[] companyModel = new CompanyModel[1];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        companyModelRefreshRef[0] = new CompanyModel(this.companies);
        companyNameSorter = new TableRowSorter<CompanyModel>(companyModelRefreshRef[0]);
        companyTable = new JTable(companyModelRefreshRef[0]) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Cancel the editing of any cell
            }
        };
        java.awt.Font itemTableFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
        companyTable.setFont(itemTableFont);
        companyTable.setRowHeight(24);
        companyTable.setSelectionBackground(new java.awt.Color(220, 220, 255));
        companyTable.setSelectionForeground(java.awt.Color.BLACK);
        companyTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        int viewRow = companyTable.getSelectedRow();
                        if (viewRow < 0) {
                            companyStatusText.setText("");
                        } else {
                            int modelRow = companyTable.convertRowIndexToModel(viewRow);
                            // Call controller method for company selection
                            controller.getAllCompanies(); // Replace with actual method if needed
                            companyStatusText.setText(String.format(" Company selected: %d.", modelRow));
                            // UI updates should be handled by controller callbacks or returned data
                        }
                    }
                });
        JScrollPane companyTableScrollPane = new JScrollPane(companyTable);
        add(companyTableScrollPane);
        JPanel companyFormPanel = new JPanel(new SpringLayout());
        JLabel companyFilterTextLabel = new JLabel("Filter Text:", SwingConstants.TRAILING);
        companyFilterTextLabel.setPreferredSize(new Dimension(10, 10));
        companyFormPanel.add(companyFilterTextLabel);
        companyFilterText = new JTextField();
        // Whenever companyFilterText changes, invoke filterCompaniesTable.
        companyFilterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        filterCompaniesTable();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        filterCompaniesTable();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        filterCompaniesTable();
                    }
                });

        companyFilterTextLabel.setLabelFor(companyFilterText);
        companyFormPanel.add(companyFilterText);
        JLabel companyNotesLabel = new JLabel("Notes:", SwingConstants.TRAILING);
        companyNotesLabel.setPreferredSize(new Dimension(50, 50));
        companyNotesLabel.setLabelFor(companyStatusText);
        companyFormPanel.add(companyNotesLabel);
        companyStatusText = new JTextArea("History for", 1, 4);
        companyStatusText.setPreferredSize(new Dimension(50, 50));
        companyStatusText.setEditable(true);
        JScrollPane scrollPane1 = new JScrollPane(companyStatusText);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        companyFormPanel.add(companyNotesLabel);
        companyFormPanel.add(scrollPane1);
        SpringUtilites.makeCompactGrid(companyFormPanel, 2, 2, 6, 6, 6, 6);

        // --- FIX: Declare and initialize l12, l22, and form2 for the second
        // filter/notes panel ---
        JPanel itemFormPanel = new JPanel(new SpringLayout());
        JLabel itemFilterTextLabel = new JLabel("Filter Text:", SwingConstants.TRAILING);
        itemFilterTextLabel.setPreferredSize(new Dimension(10, 10));
        itemFilterText = new JTextField();
        JLabel itemNotesLabel = new JLabel("Notes:", SwingConstants.TRAILING);
        itemNotesLabel.setPreferredSize(new Dimension(50, 50));
        itemStatusText = new JTextArea("History for", 1, 4);
        itemStatusText.setPreferredSize(new Dimension(50, 50));
        itemStatusText.setEditable(true);
        JScrollPane scrollPane12 = new JScrollPane(itemStatusText);
        scrollPane12.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Add all 4 components in correct order
        itemFormPanel.add(itemFilterTextLabel);
        itemFormPanel.add(itemFilterText);
        itemFormPanel.add(itemNotesLabel);
        itemFormPanel.add(scrollPane12);

        // Only show items for the first company on startup
        int firstCompanyId = companies.get(0).getCompanyId();
        List<Item> firstCompanyItems = new java.util.ArrayList<>();
        for (Item item : items) {
            if (item.getCompany() != null && item.getCompany().getCompanyId() == firstCompanyId) {
                firstCompanyItems.add(item);
            }
        }
        itemModelRefreshRef[0] = new ItemModel(firstCompanyItems, 0);
        itemNameSorter = new TableRowSorter<ItemModel>(itemModelRefreshRef[0]);
        itemTable = new JTable(itemModelRefreshRef[0]) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return FALSE; // Disallow the editing of any cell
            }
        };
        itemTable.setRowSorter(itemNameSorter);
        itemTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        if (event.getValueIsAdjusting()) {
                            return;
                        }
                        int viewRow = itemTable.getSelectedRow();
                        if (viewRow < 0) {
                            itemStatusText.setText("");
                        } else {
                            int modelRow = itemTable.convertRowIndexToModel(viewRow);
                            // Retrieve item notes from database and display in notes field
                            String notes = controller.getItemNotesForRow(modelRow);
                            itemStatusText.setText(notes != null ? notes : "No notes found.");
                        }
                    }
                });
        // Set up filter and itemStatusText listeners
        itemFilterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        filterItemsTable();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        filterItemsTable();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        filterItemsTable();
                    }
                });
        itemFilterTextLabel.setLabelFor(itemFilterText);
        itemNotesLabel.setLabelFor(itemStatusText);
        SpringUtilites.makeCompactGrid(itemFormPanel, 2, 2, 6, 6, 6, 6);
        // Now that table2 is fully initialized, add it to the panel above the
        // filter/notes
        JScrollPane itemTableScrollPane = new JScrollPane(itemTable);
        add(itemTableScrollPane);
        add(itemFormPanel);
    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void filterCompaniesTable() {
        RowFilter<CompanyModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(companyFilterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        companyNameSorter.setRowFilter(rf);
    }

    private void filterItemsTable() {
        RowFilter<ItemModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(itemFilterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        itemNameSorter.setRowFilter(rf);
    }

    class CompanyModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private String[] columnNames = {
                "Company Name"
        };

        /*
         * private String[] columnNames = {"Company Id",
         * // "Company Name",
         * };
         */
        private List<Company> company;
        private Object[][] data;

        public CompanyModel(List<Company> company) {
            this.company = company;
            int listSize = company.size();
            data = new Object[listSize][1];
            for (int i = 0; i < listSize; i++) {
                // data[i][0]=(Object)company.get(i).getCompanyId();
                data[i][0] = (Object) company.get(i).getCompanyName();
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
         * editor for each cell. If we didn't implement this method,
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
            // Note that the data/cell address is constant,
            // no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        public void updateModel() {

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

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }

    }

    public static class ItemModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private String[] columnNames = {
                "Item Name",
                "Total(s)"
        };
        @SuppressWarnings("deprecation")

        private List<Item> items;
        private Object[][] data;

        public ItemModel(List<Item> items, int index) {
            this.items = items;
            int listSize = items.size();
            data = new Object[listSize][2];
            for (int i = 0; i < listSize; i++) {
                data[i][0] = items.get(i).getItemName();
                data[i][1] = items.get(i).getQuantity();
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
