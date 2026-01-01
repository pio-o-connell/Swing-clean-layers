package concordia;

import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;
import javax.swing.JSeparator;
import javax.swing.BoxLayout;

//import com.mysql.jdbc.Connection;

/*------------------------------------------------------------------------------------------------------------------*/
// AdminPanel Class facilitates the entry of item purchased transaction in the Concordia Class.
// It's necessary to select a Supplier,then select an item from the item tables.
// Unfortunately, there are issues setting a default selection in the windows on startup - this would guarantee the
// system is in a steady state.It is but just not displayed.
//						The particular row selected in both the history and item tables 
//would be reflected in the 'New Inventory to add' panel to facilitate easy entry by the user.Selecting an item from 
//the History table has the effect of updating the edit controls.
// 												Most Suppliers are normally'returning' customers i.e. historically 
//a particular manufacturer's item purchased is normally from a previous supplier.
// So,selecting a row in the History table has the effect of updating the data entry controls. 
//
// 
// There should be discounts for returning customers(loyalty schemes in operation) so the application could prove useful.
// An 'Add' button facilitates the entry of new transaction to the system.
// 
// An 'Update' button facilitates the updating of an existing record.This is still not implemented.The database elements
// are non-editable. Clicking the update button should have the effect of displaying a custom modal dialog, where user is
// restricted in allowable entries.
//
// There is no validity checks on 'New Transactions/Itms To Add'. This should have been implemented using custom controls i.e.
// data spinner especially.
//
// It's not possible to manually edit the table fields, risk of data inconsistency very high.
// A 'New Stock Item' allows the user to enter a new item of a particular manufacturer from a particular supplier.
// 
// A  New Item button allows user to enter new company details and item details from said company.Adding a 'New Item'
// is logically very similiar to adding a transaction. User creates new item only when there is a definite transaction.
// Both history and items databases then updated to reflect this.
//
// If the User Interface was fully completed it should include a calendar on the dash. The date has proven v.difficult
// with JTextStrings.
/*-------------------------------------------------------------------------------------------------------------------*/
import concordia.controller.InventoryController;

public class AdminPanel extends JPanel {
    /**
     * Set all main fields in the DetailsPanel.
     * Call this after reading values from the database.
     */
    public void setFields(String name, String location, String supplier, String delivery, String amount) {
        System.out.println("[DetailsPanel.setFields] name=" + name + ", location=" + location + ", supplier=" + supplier
                + ", delivery=" + delivery + ", amount=" + amount);
        nameField.setText(name);
        locationField.setText(location);
        supplierField.setText(supplier);
        deliveryField.setText(delivery);
        amountField.setText(amount);
    }

    // Field to display current selected company
    private JTextField currentCompanyField = new JTextField(15);

    // Add a setter for currentCompanyField
    public void setCurrentCompanyField(String name) {
        currentCompanyField.setText(name);
    }

    private static final long serialVersionUID = 1L;
    private EventListenerList listenerList = new EventListenerList();

    // new
    private JTextField nameField = new JTextField("Name", 10);
    private JTextField locationField = new JTextField("Location", 10);
    private JTextField supplierField = new JTextField("Supplier", 10);
    private JTextField deliveryField = new JTextField("Delivery", 10);
    private JTextField amountField = new JTextField("Amount", 10);
    private JTextArea notesArea = new JTextArea(5, 20); // 5 rows, 20 columns

    private JTextField reportDeliveryFrom = new JTextField(8);
    private JTextField reportDeliveryTo = new JTextField(8);

    private JLabel statusLabel = new JLabel("Status: Waiting ");

    // Define all required labels
    private JLabel txCompanyNameLabel = new JLabel("Transaction Company Name:");
    private JLabel currentCompanyLabel = new JLabel("Current Company:");
    private JLabel nameLabel = new JLabel("Service Name:");
    private JLabel providerNameLabel = new JLabel("Supplier:");
    private JLabel locationLabel = new JLabel("Location:");
    private JLabel deliveryLabel = new JLabel("Date:");
    private JLabel quantityLabel = new JLabel("Quantity:");
    private JLabel spacerLabel = new JLabel("                ");

    // Define all required buttons
    private JButton addBtn = new JButton("Add");
    private JButton removeBtn = new JButton("Remove");
    private JButton updateBtn = new JButton("Update");
    private JButton updateItemBtn = new JButton("Update Item");
    private JButton deleteBtn = new JButton("Delete");
    private JButton itemReportBtn = new JButton("Item Report");
    private JButton itemDeliveredReportBtn = new JButton("Delivered Report");
    private JButton backupBtn = new JButton("Backup");
    private JButton restoreBtn = new JButton("Restore");
    private JButton serialBackupBtn = new JButton("Serial Backup");
    private JButton serialRestoreBtn = new JButton("Serial Restore");

    private final InventoryController controller;

    public AdminPanel(InventoryController controller) {
        // Add button action listeners to call controller methods
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Example: call controller to add item/history
                // controller.addItem(...); // Fill with actual parameters from fields
            }
        });
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Example: call controller to update item/history
                // controller.updateHistory(...); // Fill with actual parameters from fields
            }
        });
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Example: call controller to delete item/history
                // controller.deleteItem(...); // Fill with actual parameters from fields
            }
        });
        this.controller = controller;
        // Set fixed size for the DetailsPanel to prevent stretching
        Dimension panelDim = new Dimension(500, 350);
        setPreferredSize(panelDim);
        setMaximumSize(panelDim);
        setMinimumSize(panelDim);
        setAlignmentX(CENTER_ALIGNMENT);
        // Prevent text fields from stretching
        Dimension fieldDim = new Dimension(120, 24);
        nameField.setMaximumSize(fieldDim);
        supplierField.setMaximumSize(fieldDim);
        locationField.setMaximumSize(fieldDim);
        deliveryField.setMaximumSize(fieldDim);
        amountField.setMaximumSize(fieldDim);
        // notesField removed, notesArea used instead
        currentCompanyField.setMaximumSize(fieldDim);
        // Set a fixed preferred size for the panel (adjust as needed)
        setPreferredSize(new Dimension(500, 350));
        // Make DetailsPanel visually prominent as the front panel
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(new java.awt.Color(240, 240, 255));
        java.awt.Font labelFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 13);
        java.awt.Font fieldFont = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
        txCompanyNameLabel.setFont(labelFont);
        currentCompanyLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        providerNameLabel.setFont(labelFont);
        locationLabel.setFont(labelFont);
        deliveryLabel.setFont(labelFont);
        quantityLabel.setFont(labelFont);
        nameField.setFont(fieldFont);
        locationField.setFont(fieldFont);
        supplierField.setFont(fieldFont);
        deliveryField.setFont(fieldFont);
        amountField.setFont(fieldFont);
        notesArea.setFont(fieldFont);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set up the current company field (non-editable)
        currentCompanyField.setEditable(false);
        currentCompanyField.setBackground(new Color(240, 240, 240));

        // Helper to create a row panel for label/field (exactly two components per row,
        // each half width)
        int rowHeight = 28;
        int panelWidth = 500; // Should match DetailsPanel preferred width
        int halfWidth = panelWidth / 2;
        java.util.function.BiFunction<JLabel, JTextField, JPanel> row = (label, field) -> {
            JPanel p = new JPanel(new java.awt.GridLayout(1, 2));
            label.setPreferredSize(new Dimension(halfWidth, rowHeight));
            label.setMaximumSize(new Dimension(halfWidth, rowHeight));
            field.setPreferredSize(new Dimension(halfWidth, rowHeight));
            field.setMaximumSize(new Dimension(halfWidth, rowHeight));
            p.add(label);
            p.add(field);
            p.setMaximumSize(new Dimension(panelWidth, rowHeight));
            p.setPreferredSize(new Dimension(panelWidth, rowHeight));
            return p;
        };

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(row.apply(currentCompanyLabel, currentCompanyField));
        add(row.apply(nameLabel, nameField));
        add(row.apply(providerNameLabel, supplierField));
        add(row.apply(locationLabel, locationField));
        add(row.apply(deliveryLabel, deliveryField));
        add(row.apply(quantityLabel, amountField));
        // Add Notes label and area on the same horizontal line
        JLabel notesLabel = new JLabel("Notes:");
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setMaximumSize(new Dimension(halfWidth, rowHeight * 3));
        JPanel notesRow = new JPanel(new java.awt.GridLayout(1, 2));
        notesLabel.setPreferredSize(new Dimension(halfWidth, rowHeight * 3));
        notesLabel.setMaximumSize(new Dimension(halfWidth, rowHeight * 3));
        notesRow.add(notesLabel);
        notesRow.add(notesArea);
        notesRow.setMaximumSize(new Dimension(panelWidth, rowHeight * 3));
        notesRow.setPreferredSize(new Dimension(panelWidth, rowHeight * 3));
        add(notesRow);

        add(Box.createRigidArea(new Dimension(0, 16))); // ~2 blank lines

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        add(separator);

        // Panel for Add/Update/Remove buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        add(buttonPanel);
        // Add a horizontal separator below the buttons
        add(Box.createRigidArea(new Dimension(0, 16))); // ~2 blank lines
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        add(separator);

        add(Box.createRigidArea(new Dimension(0, 16))); // ~2 blank lines
        // Add vertical space after separator
        add(Box.createRigidArea(new Dimension(0, 16))); // ~2 blank lines
        // Status label
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.add(statusLabel);
        add(statusPanel);

        add(Box.createRigidArea(new Dimension(0, 16))); // ~2 blank lines
        // Add vertical space after separator

        // Panel for extra buttons
        JPanel extraButtonPanel = new JPanel();
        extraButtonPanel.setLayout(new BoxLayout(extraButtonPanel, BoxLayout.X_AXIS));
        extraButtonPanel.add(updateItemBtn);
        extraButtonPanel.add(deleteBtn);
        extraButtonPanel.add(itemReportBtn);
        extraButtonPanel.add(itemDeliveredReportBtn);
        extraButtonPanel.add(backupBtn);
        extraButtonPanel.add(restoreBtn);
        extraButtonPanel.add(serialBackupBtn);
        extraButtonPanel.add(serialRestoreBtn);
        add(extraButtonPanel);

        // (Later: add detailTable1 and detailTable2 panels here)
    }

    // Helper to get the current selected company name
    // Now handled by controller; UI should not access data directly
    private String getCurrentCompanyName() {
        // Example: return controller.getCurrentCompanyName();
        // Use controller to get the current company name if needed
        return "";
    }

    // Moved misplaced GUI layout code into the constructor
    // (No code should be here outside methods/constructors)

    public void fireDetailEvent(DetailEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == DetailListener.class) {
                ((DetailListener) listeners[i + 1]).detailEventOccured(event);
            }
        }
    }

    public void addDetailListener(DetailListener listener) {
        listenerList.add(DetailListener.class, listener);
    }

    public void removeDetailListener(DetailListener listener) {
        listenerList.remove(DetailListener.class, listener);
    }

    public Date convertStringToDate(String dateString) {
        Date date = null;
        Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String s = formatter.format(date);

        return date;

    }

}
