package Concordia;

/*------------------------------------------------------------------------------------------------------------------*/
//TableWindow2 Class retrieves the data structure (from memory) and renders the History tables.
//CompanyItemTablePanel also has 1 anonymous class containing  action listeners,called for when user selects an table item
//from History tables respectively.
// History tables is non editable - risk of data inconsistency too high
//Selecting a row in the History table has the effect of updating the data entry controls (located in the AdminPanel Class).
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
import Concordia.domain.Company;
import Concordia.domain.Item;
import Concordia.domain.User;
import Concordia.domain.history;

import Concordia.controller.InventoryController;

public class TransactionHistoryPanel extends JPanel {
    private ArrayList<history> history;
    private ArrayList<Company> companies;
    private ArrayList<Item> items, item;

    private static final long serialVersionUID = 1L;
    private boolean DEBUG = false;
    public static JTable transactionHistoryTable;
    private JTextField historyTransactionFilterText;
    private JTextArea historyTransactionStatusText;
    public TableRowSorter<MyTableModel> historyTransactionNameSorter;
    public int nmrRowsTable3;
    public MyTableModel transactionHistoryTableModel;
    private final InventoryController controller;

        public TransactionHistoryPanel(ArrayList<Item> items, ArrayList<Company> companies, ArrayList<history> history,
            InventoryController controller) {
        super();
        setOpaque(true);
        setBackground(new java.awt.Color(255, 240, 240));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setPreferredSize(new java.awt.Dimension(400, 300));
        this.history = history;
        this.items = items;
        this.companies = companies;
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        transactionHistoryTableModel = new MyTableModel(companies.get(0).getItems().get(0).getHistory(), 0);
        historyTransactionNameSorter = new TableRowSorter<MyTableModel>(transactionHistoryTableModel);
        transactionHistoryTable = new JTable(transactionHistoryTableModel) {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Disallow the editing of any cell
            }
        };
        java.awt.Font tableFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
        transactionHistoryTable.setFont(tableFont);
        transactionHistoryTable.setRowHeight(24);
        transactionHistoryTable.setSelectionBackground(new java.awt.Color(255, 220, 220));
        transactionHistoryTable.setSelectionForeground(java.awt.Color.BLACK);
        transactionHistoryTable.setRowSorter(historyTransactionNameSorter);
        transactionHistoryTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        transactionHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        transactionHistoryTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        if (event.getValueIsAdjusting()) {
                            return;
                        }
                        int viewRow = transactionHistoryTable.getSelectedRow();
                        if (viewRow < 0) {
                            historyTransactionStatusText.setText("");
                        } else {
                            int modelRow = transactionHistoryTable.convertRowIndexToModel(viewRow);
                            // Retrieve record from database and display in notes field
                            String notes = controller.getHistoryNotesForRow(modelRow);
                            historyTransactionStatusText.setText(notes != null ? notes : "No notes found.");
                        }
                    }
                });

        JScrollPane transactionHistoryTableScrollPane = new JScrollPane(transactionHistoryTable);
        add(transactionHistoryTableScrollPane);
        JPanel transactionHistoryFormPanel = new JPanel(new SpringLayout());
        JLabel historyTransactionFilterTextLabel = new JLabel("Filter Text:", SwingConstants.TRAILING);
        historyTransactionFilterTextLabel.setPreferredSize(new Dimension(10, 10));
        transactionHistoryFormPanel.add(historyTransactionFilterTextLabel);
        historyTransactionFilterText = new JTextField();
        // Whenever filterText changes, invoke newFilter.
        historyTransactionFilterText.getDocument().addDocumentListener(
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

        historyTransactionFilterTextLabel.setLabelFor(historyTransactionFilterText);
        transactionHistoryFormPanel.add(historyTransactionFilterText);
        historyTransactionStatusText = new JTextArea("History for", 1, 4);
        JLabel historyTransactionNotesLabel = new JLabel("Notes:", SwingConstants.TRAILING);
        historyTransactionNotesLabel.setPreferredSize(new Dimension(50, 50));
        historyTransactionNotesLabel.setLabelFor(historyTransactionStatusText);
        transactionHistoryFormPanel.add(historyTransactionNotesLabel);
        historyTransactionStatusText.setPreferredSize(new Dimension(50, 50));
        historyTransactionStatusText.setEditable(true);
        JScrollPane scrollPane12 = new JScrollPane(historyTransactionStatusText);
        scrollPane12.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        transactionHistoryFormPanel.add(scrollPane12);
        SpringUtilites.makeCompactGrid(transactionHistoryFormPanel, 2, 2, 6, 6, 6, 6);
        add(transactionHistoryFormPanel);
    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        // If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(historyTransactionFilterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        historyTransactionNameSorter.setRowFilter(rf);
    }

    public static class MyTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private String[] columnNames = {
                "Delivery Date",
                "Location",
                "Quantity",
                "Supplier" };
        private ArrayList<history> history;

        @SuppressWarnings("deprecation")
        private Object[][] data;

        public MyTableModel(ArrayList<history> history, int index1) {
            this.history = history;
            int index = index1;
            int listSize = history.size();
            data = new Object[listSize][4];
            for (int i = 0; i < listSize; i++) {
                data[i][0] = (Object) history.get(i).getDeliveryDate();
                data[i][1] = (Object) history.get(i).getLocation();
                data[i][2] = (Object) history.get(i).getAmount();
                data[i][3] = (Object) history.get(i).getSupplier();
            }
        }

        public Object[][] convertTo2D() {
            int listSize = history.size();
            final Object[][] data2 = new Object[listSize][4];
            for (int i = 0; i < listSize; i++) {
                data2[i][0] = (Object) history.get(i).getDeliveryDate();
                data2[i][1] = (Object) history.get(i).getLocation();
                data2[i][2] = (Object) history.get(i).getAmount();
                data2[i][3] = (Object) history.get(i).getSupplier();
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
         * editor for each cell. If we didn't implement this method,
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
            // Note that the data/cell address is constant,
            // no matter where the cell appears onscreen.
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
            if (data == null || data.length == 0)
                return;
            if (row < 0 || row >= data.length)
                return;
            if (data[0] == null)
                return;
            // Defensive: check all rows for null and correct length
            if (col < 0)
                return;
            if (row >= data.length)
                return;
            if (data[row] == null || col >= data[row].length)
                return;
            data[row][col] = value;
            fireTableCellUpdated(row, col);
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

        public void set() {

        }
    }
}
