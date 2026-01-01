package concordia;

import javax.swing.JFrame;
import concordia.controller.InventoryController;

public class Mainframe extends JFrame {
    public Mainframe(String title, InventoryController controller) {
        super(title);
        setSize(1200, 700);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());

        AdminPanel adminPanel = new AdminPanel(controller);
        java.util.List<concordia.domain.Item> items = new java.util.ArrayList<>();
        if (!controller.getAllCompanies().isEmpty()) {
            java.util.Set<concordia.domain.Item> itemSet = controller.getAllCompanies().get(0).getItems();
            items.addAll(itemSet);
        }
        java.util.List<concordia.domain.Company> companies = controller.getAllCompanies();
        java.util.List<concordia.domain.History> history = controller.getAllHistory();
        TransactionHistoryPanel transactionHistoryPanel = new TransactionHistoryPanel(items, companies, history, controller);

        // Use JSplitPane to split AdminPanel (WEST) and TransactionHistoryPanel (EAST)
        javax.swing.JScrollPane adminScrollPane = new javax.swing.JScrollPane(adminPanel);
        adminScrollPane.setPreferredSize(new java.awt.Dimension(800, 700));
        javax.swing.JScrollPane transactionScrollPane = new javax.swing.JScrollPane(transactionHistoryPanel);
        transactionScrollPane.setPreferredSize(new java.awt.Dimension(400, 700));
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, adminScrollPane, transactionScrollPane);
        splitPane.setDividerLocation(800);
        splitPane.setResizeWeight(0.67);
        splitPane.setContinuousLayout(true);
        add(splitPane, java.awt.BorderLayout.CENTER);

        // Add another TransactionHistoryPanel to SOUTH (history table)
        TransactionHistoryPanel transactionHistoryPanelSouth = new TransactionHistoryPanel(items, companies, history, controller);
        add(transactionHistoryPanelSouth, java.awt.BorderLayout.SOUTH);
    }
}

