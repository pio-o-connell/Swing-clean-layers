//-------------------------------------------------------------------
// Loads the database into the memory structure.
//-------------------------------------------------------------------


package WareHouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import WareHouse.Company;
import WareHouse.Item;
import WareHouse.User;
import WareHouse.history;
import WareHouse.Mainframe;
import WareHouse.DetailsPanel;

public final class Databases {
    // Resets and repopulates the database with gardening test data
    public void resetAndPopulateGardeningTestData(Connection con) throws SQLException {
        java.util.Random rand = new java.util.Random();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        // Reset all data
        PreparedStatement delHist = con.prepareStatement("DELETE FROM history");
        delHist.executeUpdate();
        PreparedStatement delItem = con.prepareStatement("DELETE FROM item");
        delItem.executeUpdate();
        PreparedStatement delComp = con.prepareStatement("DELETE FROM company");
        delComp.executeUpdate();

        // Add only two companies: one original and one gardening company
        String[] companyNames = {"Kanturk-Services", "GreenThumb Supplies"};
        int[] companyIds = {44008177, 77008177};
        for (int i = 0; i < companyNames.length; i++) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO company (company_id, company_name) VALUES (?, ?)");
            ps.setInt(1, companyIds[i]);
            ps.setString(2, companyNames[i]);
            ps.executeUpdate();
            ps.close();
        }

        // Add items for Kanturk-Services (4 items)
        String[] kanturkItems = {"Shovel", "Rake", "Lawn Mower", "Hose"};
        int kanturkCompanyId = 44008177;
        int kanturkItemIdBase = 44020000;
        for (int j = 0; j < kanturkItems.length; j++) {
            int itemId = kanturkItemIdBase + j;
            String itemNote = "Kanturk item: " + kanturkItems[j] + " - essential for every garden.";
            PreparedStatement ps = con.prepareStatement("INSERT INTO item (item_id, item_name, company_id, quantity, notes) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, itemId);
            ps.setString(2, kanturkItems[j]);
            ps.setInt(3, kanturkCompanyId);
            ps.setInt(4, 10 + rand.nextInt(90));
            ps.setString(5, itemNote);
            ps.executeUpdate();
            ps.close();

            // Add 4 history records per item
            String[] locations = {"Greenhouse", "Garden Shed", "Nursery", "Compost Area"};
            String[] providers = {"GardenWorld", "PlantDepot", "SeedMasters", "ToolTown"};
            String[] notes = {
                "Planted in spring for best results",
                "Keep soil moist and well-drained",
                "Fertilize every two weeks"
            };
            for (int h = 0; h < 4; h++) {
                int historyId = 21000000 + j * 10 + h;
                int amount = 5 + rand.nextInt(20);
                String location = locations[rand.nextInt(locations.length)];
                String provider = providers[rand.nextInt(providers.length)];
                String deliveryDate = sdf.format(new java.util.Date(System.currentTimeMillis() - rand.nextInt(1000 * 60 * 60 * 24 * 365)));
                String note = notes[h % notes.length] + " (" + kanturkItems[j] + ")";
                PreparedStatement psHist = con.prepareStatement(
                    "INSERT INTO history (history_id, item_id, amount, location, provider, delivery_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                psHist.setInt(1, historyId);
                psHist.setInt(2, itemId);
                psHist.setInt(3, amount);
                psHist.setString(4, location);
                psHist.setString(5, provider);
                psHist.setString(6, deliveryDate);
                psHist.setString(7, note);
                psHist.executeUpdate();
                psHist.close();
            }
        }

        // Add items for GreenThumb Supplies (4 items)
        String[] gardeningItems = {"Compost Bin", "Garden Hoe", "Tomato Seeds", "Watering Can"};
        int gardeningCompanyId = 77008177;
        int itemIdBase = 44010000;
        for (int j = 0; j < gardeningItems.length; j++) {
            int itemId = itemIdBase + j;
            String itemNote = "Gardening item: " + gardeningItems[j] + " - essential for every garden.";
            PreparedStatement ps = con.prepareStatement("INSERT INTO item (item_id, item_name, company_id, quantity, notes) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, itemId);
            ps.setString(2, gardeningItems[j]);
            ps.setInt(3, gardeningCompanyId);
            ps.setInt(4, 10 + rand.nextInt(90));
            ps.setString(5, itemNote);
            ps.executeUpdate();
            ps.close();

            // Add 4 history records per item
            String[] locations = {"Greenhouse", "Garden Shed", "Nursery", "Compost Area"};
            String[] providers = {"GardenWorld", "PlantDepot", "SeedMasters", "ToolTown"};
            String[] notes = {
                "Planted in spring for best results",
                "Keep soil moist and well-drained",
                "Fertilize every two weeks"
            };
            for (int h = 0; h < 4; h++) {
                int historyId = 20000000 + j * 10 + h;
                int amount = 5 + rand.nextInt(20);
                String location = locations[rand.nextInt(locations.length)];
                String provider = providers[rand.nextInt(providers.length)];
                String deliveryDate = sdf.format(new java.util.Date(System.currentTimeMillis() - rand.nextInt(1000 * 60 * 60 * 24 * 365)));
                String note = notes[h % notes.length] + " (" + gardeningItems[j] + ")";
                PreparedStatement psHist = con.prepareStatement(
                    "INSERT INTO history (history_id, item_id, amount, location, provider, delivery_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                psHist.setInt(1, historyId);
                psHist.setInt(2, itemId);
                psHist.setInt(3, amount);
                psHist.setString(4, location);
                psHist.setString(5, provider);
                psHist.setString(6, deliveryDate);
                psHist.setString(7, note);
                psHist.executeUpdate();
                psHist.close();
            }
        }
    }
    // Update a history record in the database and in-memory model
    public void updateHistoryTransintoDatabase(Connection con, ArrayList<Company> Company, int companyIndex, int itemIndex, int historyIndex, String name, String location, String supplier, String delivery, int quantity) throws SQLException {
        // Update in-memory model
        history hist = Company.get(companyIndex).getItems().get(itemIndex).getHistory().get(historyIndex);
        hist.setLocation(location);
        hist.setSupplier(supplier);
        hist.setDeliveryDate(delivery);
        hist.setAmount(quantity);
        // Optionally set notes if provided (add parameter if needed)

        // Update database
        String query = "UPDATE history SET location = ?, supplier = ?, delivery_date = ?, amount = ?, notes = ? WHERE history_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, location);
        stmt.setString(2, supplier);
        stmt.setString(3, delivery);
        stmt.setInt(4, quantity);
        stmt.setString(5, hist.getNotes());
        stmt.setInt(6, hist.getHistoryId());
        stmt.executeUpdate();
    }

    Connection con;
    ArrayList<Company> Company = new ArrayList<Company>();
    ArrayList<history> History = new ArrayList<history>();
    ArrayList<Item> Item = new ArrayList<Item>();
    ArrayList<User> User = new ArrayList<User>();

    Databases(Connection con) throws Exception {

        this.con = con;

    }

    // Loads the database tables into memory (Company, Item, User, History)
    public boolean init(Connection con, ArrayList<Company> Company) {
        this.con = con;
        this.Company = Company;
        try {
            // Drop tables if they exist
            String[] dropTables = {
                "DROP TABLE IF EXISTS history",
                "DROP TABLE IF EXISTS item",
                "DROP TABLE IF EXISTS users",
                "DROP TABLE IF EXISTS company"
            };
            for (String sql : dropTables) {
                try {
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Create tables
            String createCompany = "CREATE TABLE company (Company_ID INT NOT NULL, Company_title CHAR(25) NULL, company_name VARCHAR(255) NULL, PRIMARY KEY(Company_ID)) ENGINE = InnoDB;";
            String createUsers = "CREATE TABLE users (User_ID INT NOT NULL, User_Name CHAR(25) NULL, User_Password CHAR(25) NOT NULL, Company_ID INT NOT NULL, PRIMARY KEY(User_ID)) ENGINE = InnoDB;";
            String createItem = "CREATE TABLE item (Item_ID INT NOT NULL AUTO_INCREMENT, Company_ID INT NOT NULL, quantity INT NULL, item_name CHAR(25) NULL, Location CHAR(25) NULL, Notes VARCHAR(200) NULL, PRIMARY KEY(Item_ID,Company_ID)) ENGINE = InnoDB;";
            String createHistory = "CREATE TABLE history (history_id INT NOT NULL AUTO_INCREMENT, item_ID INT NOT NULL, amount INT NULL, location CHAR(25) NULL, provider CHAR(25) NULL, delivery_date CHAR(25) NULL, notes VARCHAR(200) NULL, PRIMARY KEY(history_id)) ENGINE = InnoDB;";
            String[] createTables = {createCompany, createUsers, createItem, createHistory};
            for (String sql : createTables) {
                try {
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Now repopulate with test data
            resetAndPopulateGardeningTestData(con);

            // Load users
            ArrayList<User> users = new ArrayList<>();
            PreparedStatement userStmt = con.prepareStatement("SELECT * FROM users");
            ResultSet userResult = userStmt.executeQuery();
            while (userResult.next()) {
                users.add(new User(userResult.getInt(1), userResult.getInt(4), userResult.getString(2), userResult.getString(3)));
            }

            // Load companies and their items
            PreparedStatement companyStmt = con.prepareStatement("SELECT * FROM company");
            ResultSet companyResult = companyStmt.executeQuery();
            while (companyResult.next()) {
                int companyId = companyResult.getInt(1);
                String companyName = companyResult.getString("company_name");

                // Load items for this company
                ArrayList<Item> companyItems = new ArrayList<>();
                PreparedStatement itemStmt = con.prepareStatement("SELECT * FROM item WHERE Company_ID = ?");
                itemStmt.setInt(1, companyId);
                ResultSet itemsResult = itemStmt.executeQuery();
                while (itemsResult.next()) {
                    int itemId = itemsResult.getInt(1);
                    int quantity = itemsResult.getInt(3);
                    String itemName = itemsResult.getString("item_name");
                    String itemNotes = null;
                    try { itemNotes = itemsResult.getString("notes"); } catch (Exception e) { itemNotes = null; }

                    // Load history for this item
                    ArrayList<history> historyList = new ArrayList<>();
                    PreparedStatement histStmt = con.prepareStatement("SELECT * FROM HISTORY WHERE item_id = ?");
                    histStmt.setInt(1, itemId);
                    ResultSet histResult = histStmt.executeQuery();
                    while (histResult.next()) {
                        int historyId = histResult.getInt(1);
                        int amount = histResult.getInt(3);
                        String location = histResult.getString(4);
                        String provider = histResult.getString(5);
                        String deliveryDate = histResult.getString(6);
                        String historyNotes = null;
                        try { historyNotes = histResult.getString("notes"); } catch (Exception e) { historyNotes = null; }
                        historyList.add(new history(historyId, itemId, amount, location, provider, deliveryDate, historyNotes));
                    }
                    companyItems.add(new Item(itemId, companyId, quantity, itemName, itemNotes, historyList));
                }
                Company.add(new Company(companyId, companyName, companyItems, users));
            }
            return true;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return false;
        }
    }

    public void setup(Connection con, ArrayList<Company> Company) throws SQLException {
        this.con = con;
        this.Company = Company;

        //select distinct items for the company
        try {

            // 1. Delete all old history data
            PreparedStatement deleteHistory = (PreparedStatement) con.prepareStatement("DELETE FROM HISTORY");
            deleteHistory.executeUpdate();

            // 2. Load items
            PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT * from ITEM");
            ResultSet itemsResult = statement.executeQuery();
            java.util.Random rand = new java.util.Random();
            // Gardening-themed locations, providers, and notes
            String[] locations = {"Greenhouse", "Garden Shed", "Nursery", "Compost Area", "Tool Shed", "Flower Bed"};
            String[] providers = {"GardenWorld", "PlantDepot", "SeedMasters", "ToolTown"};
            String[] datePool = {"2025-03-15", "2025-04-10", "2025-05-05", "2025-06-01", "2025-07-12", "2025-08-18", "2025-09-22", "2025-10-30"};
            String[] gardeningNotes = {
                "Planted in spring for best results",
                "Keep soil moist and well-drained",
                "Fertilize every two weeks",
                "Store in a cool, dry place",
                "Prune regularly to encourage growth",
                "Harvest before first frost",
                "Check for pests weekly",
                "Water early in the morning"
            };

            int historyIdCounter = 1000000;
            while (itemsResult.next()) {
                int itemId = itemsResult.getInt(1);
                int companyId = itemsResult.getInt(2);
                int quantity = itemsResult.getInt(3);
                String itemName = itemsResult.getString(4);
                String itemNotes = null;
                try { itemNotes = itemsResult.getString("notes"); } catch (Exception e) { itemNotes = null; }
                History = new ArrayList<history>();

                // 3. Generate a random number of history records for each item
                int numHistories = 2 + rand.nextInt(4); // 2-5 histories per item
                for (int h = 0; h < numHistories; h++) {
                    int historyId = historyIdCounter++;
                    int amount = 10 + rand.nextInt(90); // 10-99
                    String location = locations[rand.nextInt(locations.length)];
                    String provider = providers[rand.nextInt(providers.length)];
                    String deliveryDate = datePool[rand.nextInt(datePool.length)];
                    String historyNotes = gardeningNotes[rand.nextInt(gardeningNotes.length)];

                    // Insert into DB
                    PreparedStatement insertHistory = (PreparedStatement) con.prepareStatement(
                        "INSERT INTO HISTORY (history_id, item_id, amount, location, provider, delivery_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    );
                    insertHistory.setInt(1, historyId);
                    insertHistory.setInt(2, itemId);
                    insertHistory.setInt(3, amount);
                    insertHistory.setString(4, location);
                    insertHistory.setString(5, provider);
                    insertHistory.setString(6, deliveryDate);
                    insertHistory.setString(7, historyNotes);
                    insertHistory.executeUpdate();

                    // Add to in-memory model
                    History.add(new history(historyId, itemId, amount, location, provider, deliveryDate, historyNotes));
                }
                Item.add(new Item(itemId, companyId, quantity, itemName, itemNotes, History));
            }
            // Create the users - only one
            PreparedStatement statement3 = (PreparedStatement) con.prepareStatement("select * from users ");
            ResultSet result3 = statement3.executeQuery();
            while (result3.next()) {
                User.add(new User(result3.getInt(1), result3.getInt(4), result3.getString(2), result3.getString(3)));
            }

            // Create the Company - only one
            PreparedStatement statement4 = (PreparedStatement) con.prepareStatement("select * from company ");
            ResultSet result4 = statement4.executeQuery();
            while (result4.next()) {
                System.out.println("\n" + result4.getInt(1) + " name:" + result4.getString(2));
                Company.add(new Company(result4.getInt(1), result4.getString(2), Item, User));
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

    }

    public void deleteItemTransintoDatabase(Connection con, ArrayList<Company> Company) throws SQLException {

        try {

            ArrayList<Item> itemPointer = Company.get(Mainframe.companyIndex).getItems();

            itemPointer.remove(Mainframe.itemIndex);
            // remove from databases	
            // item database
            String query = "delete from item where Item_id = ?";
            PreparedStatement preparedStmt1 = (PreparedStatement) con.prepareStatement(query);
            preparedStmt1.setInt(1, itemPointer.get(Mainframe.historyIndex).getItemId());
            preparedStmt1.execute();

            //history database
            String query1 = "delete from history where item_id = ?";

            PreparedStatement preparedStmt11 = (PreparedStatement) con.prepareStatement(query1);
            preparedStmt11.setInt(1, itemPointer.get(Mainframe.itemIndex).getItemId());
            preparedStmt11.execute();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteHistoryTransintoDatabase(Connection con, ArrayList<Company> Company) throws SQLException {
        this.con = con;
        try {

            ArrayList<history> historyPointer = Company.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getHistoryItem();
            System.out.println("history  pointer..>" + historyPointer.get(Mainframe.historyIndex).getHistoryId());
            historyPointer.remove(Mainframe.historyIndex);
            // remove from history database
            String query = "delete from history where history_id = ?";
            PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
            preparedStmt.setInt(1, historyPointer.get(Mainframe.historyIndex).getHistoryId());

            preparedStmt.execute();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTransactionintoDatabase(Connection con, ArrayList<Company> Company) throws SQLException {

        try {
            PreparedStatement statement;
            statement = (PreparedStatement) con.prepareStatement("INSERT  INTO  history(ITEM_id,AMOUNT,LOCATION,Supplier,DELIVERY_DATE,notes)  VALUES  (?,?,?,?,?,?)");
            int temp = maindriver.Company.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getItemId();
            String location = DetailsPanel.locationField.getText();
            String supplier = DetailsPanel.supplierField.getText();
            String delivery = DetailsPanel.deliveryField.getText();
            String tempAmount = DetailsPanel.amountField.getText();
            String notes = DetailsPanel.notesArea.getText();

            int Amount = Integer.parseInt(tempAmount);

            statement.setInt(1, maindriver.Company.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getItemId());
            statement.setInt(2, Amount);
            statement.setString(3, location);
            statement.setString(4, supplier);
            statement.setString(5, delivery);
            statement.setString(6, notes);
            statement.executeUpdate();
            int total = maindriver.Company.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).getQuantity();
            total = total + Amount;

            maindriver.Company.get(Mainframe.companyIndex).getItems().get(Mainframe.itemIndex).setQuantity(Amount);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertNewItemTransintoDatabase(Connection con, ArrayList<Company> Company) throws SQLException {
        try {
            PreparedStatement statement = (PreparedStatement) con.prepareStatement("INSERT  INTO  item(company_id,quantity,itemName,notes)  VALUES  (?,?,?,?)");
            int companyId = maindriver.Company.get(Mainframe.companyIndex).getCompanyId();
            String tempAmount = DetailsPanel.amountField.getText();
            int Amount = Integer.parseInt(tempAmount);
            String itemName = DetailsPanel.nameField.getText();
            String notes = DetailsPanel.notesArea.getText();
            statement.setInt(1, companyId);
            statement.setInt(2, Amount);
            statement.setString(3, itemName);
            statement.setString(4, notes);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Now need to determine autogenerated item_id
        int item_id = 0;
        try {
            //		String query = "select * from item where item_id=last_insert_id()";
            String query = "select item_id from item order by item_id desc";
            PreparedStatement statement3 = (PreparedStatement) con.prepareStatement(query);
            ResultSet result3 = statement3.executeQuery();

            while (result3.next()) {
                item_id = result3.getInt(1);
                break;
            }

            System.out.println("Auto generated id" + item_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Item(int itemId,int companyId,int quantity,String itemName,String notes,ArrayList<history> historyItem)
        // now to add the item to the items array
        int companyId = maindriver.Company.get(Mainframe.companyIndex).getCompanyId();
        String tempAmount = DetailsPanel.amountField.getText();
        int Amount = Integer.parseInt(tempAmount);
        String itemName = DetailsPanel.nameField.getText();
        String notes = DetailsPanel.notesArea.getText();
        ArrayList<history> tempHistory = new ArrayList<history>();
        Item tempItem = new Item(item_id, companyId, Amount, itemName, notes, tempHistory);
        ArrayList<Item> currentItemPointer = maindriver.Company.get(Mainframe.companyIndex).getItems();
        currentItemPointer.add(tempItem);
        Mainframe.itemIndex = (Company.get(Mainframe.companyIndex).getItems().size());

        // Now to create entry in the history database
        try {
            PreparedStatement statement = (PreparedStatement) con.prepareStatement("INSERT  INTO  history(ITEM_id,AMOUNT,LOCATION,Supplier,DELIVERY_DATE,notes)  VALUES  (?,?,?,?,?,?)");
            int temp = item_id;
            String location = DetailsPanel.locationField.getText();
            String supplier = DetailsPanel.supplierField.getText();
            String delivery = DetailsPanel.deliveryField.getText();
            String tempAmount1 = DetailsPanel.amountField.getText();
            String notes1 = DetailsPanel.notesArea.getText();
            int Amount1 = Integer.parseInt(tempAmount);
            System.out.println("5 total" + temp + location + supplier + delivery + tempAmount1);
            statement.setInt(1, item_id);
            statement.setInt(2, Amount);
            statement.setString(3, location);
            statement.setString(4, supplier);
            statement.setString(5, delivery);
            statement.setString(6, notes1);
            statement.executeUpdate();
            // Now to retrieve the auto generated history_id
            int history_id = 0;
            String query = "select item_id from item order by item_id desc";
            PreparedStatement statement3 = (PreparedStatement) con.prepareStatement(query);
            ResultSet result3 = statement3.executeQuery();
            while (result3.next()) {
                history_id = result3.getInt(1);
                break;
            }
            // update history array
            int itemSize = maindriver.Company.get(Mainframe.companyIndex).getItems().size();
            ArrayList<history> currentItemHistoryPointer = maindriver.Company.get(Mainframe.companyIndex).getItems().get(itemSize - 1).getHistory();
            /*      currentItemHistoryPointer.get(0).getHistoryId();
                          currentItemHistoryPointer.get(0).setItemId(item_id);
                          currentItemHistoryPointer.get(0).setAmount(Amount);
                          currentItemHistoryPointer.get(0).setLocation(location);
                          currentItemHistoryPointer.get(0).setSupplier(supplier);
                          currentItemHistoryPointer.get(0).setDeliveryDate(delivery);
                          currentItemHistoryPointer.get(0).setNotes(notes1);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
