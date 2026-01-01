
package concordia.repository;
import concordia.annotations.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;

@Repository
public class BackupRepository {
    private Connection con;
    public BackupRepository(Connection con) {
        this.con = con;
    }

    public void backup(ArrayList<Company> Concordia) throws SQLException {
        try {
            PreparedStatement statement = (PreparedStatement) con.prepareStatement("DROP DATABASE if exists BackupConcordia");
            statement.executeUpdate();

            statement = (PreparedStatement) con.prepareStatement("CREATE DATABASE BackupConcordia");
            statement.executeUpdate();

            statement = (PreparedStatement) con.prepareStatement("USE BackupConcordia");
            statement.executeUpdate();

            String query = "CREATE TABLE Company(" +
                    "Company_ID INT NOT NULL," +
                    "Company_title CHAR(25) NULL," +
                    "PRIMARY KEY(Company_ID)" +
                    ")" +
                    "ENGINE = InnoDB  ";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            statement = (PreparedStatement) con.prepareStatement(
                "ALTER TABLE Item ADD UNIQUE KEY uq_item_id (Item_ID)"
            );
            statement.executeUpdate();

            query = " CREATE TABLE Users(" +
                    "User_ID INT NOT NULL," +
                    "User_Name CHAR(25) NULL," +
                    "User_Password CHAR(25) NOT NULL," +
                    "Company_ID INT NOT NULL," +
                    "PRIMARY KEY(User_ID)," +
                    "CONSTRAINT fk_companies1 FOREIGN KEY(Company_ID)" +
                    "REFERENCES Company(Company_ID)" +
                    " )" +
                    " ENGINE = InnoDB";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            query = "CREATE TABLE Item(" +
                    "Item_ID INT NOT NULL AUTO_INCREMENT," +
                    "Company_ID INT NOT NULL," +
                    "quantity INT NULL," +
                    "itemName CHAR(25) NULL," +
                    "PRIMARY KEY(Item_ID)," +
                    "CONSTRAINT fk_companies FOREIGN KEY(Company_ID)" +
                    "REFERENCES Company(Company_ID)" +
                    ")" +
                    "ENGINE = InnoDB";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            query = "CREATE TABLE History(" +
                    "history_id INT NOT NULL AUTO_INCREMENT," +
                    "item_ID INT NOT NULL," +
                    "amount INT NULL," +
                    "location CHAR(25) NULL," +
                    "Supplier CHAR(25) NULL," +
                    "Delivery_Date DATE NULL," +
                    "PRIMARY KEY(history_id,item_ID)," +
                    "CONSTRAINT fk_items FOREIGN KEY(item_ID) " +
                    "REFERENCES Item(Item_ID)" +
                    ")" +
                    "ENGINE = InnoDB ";

            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            // for Company
            System.out.println(Concordia.get(0).getCompanyId());
            System.out.println("Sizeof Company Array" + Concordia.size());

            // Convert Set to List for items
            java.util.Set<concordia.domain.Item> itemSet0 = Concordia.get(0).getItems();
            java.util.List<concordia.domain.Item> itemList0 = new java.util.ArrayList<>(itemSet0);
            System.out.println(itemList0.isEmpty() ? "No items" : itemList0.get(0).getItemName());
            System.out.println("Sizeof items array" + itemList0.size());

            // Convert Set to List for history
            java.util.List<concordia.domain.History> historyList0 = itemList0.isEmpty() ? new java.util.ArrayList<>() : itemList0.get(0).getHistory();
            System.out.println(historyList0.isEmpty() ? "No history" : historyList0.get(0).getLocation());
            System.out.println("Sizeof history array" + historyList0.size());

            java.util.List<concordia.domain.History> historyList1 = itemList0.size() > 1 ? itemList0.get(1).getHistory() : new java.util.ArrayList<>();
            System.out.println("Sizeof history array" + historyList1.size());
            System.out.println(historyList1.isEmpty() ? "No historyId" : historyList1.get(0).getHistoryId());

            // Convert Set to List for users
            java.util.Set<concordia.domain.User> userSet = Concordia.get(0).getUsers();
            java.util.List<concordia.domain.User> userList = new java.util.ArrayList<>(userSet);
            System.out.println(userList.isEmpty() ? "No users" : userList.get(0).getPassword());

            // backup the data to the backup
            for (int i = 0; i < Concordia.size(); i++) { // will be only one company here
                statement = (PreparedStatement) con.prepareStatement("INSERT INTO COMPANY(Company_ID,Company_title)  VALUES  (?,?)");
                statement.setInt(1, Concordia.get(i).getCompanyId());
                statement.setString(2, Concordia.get(i).getCompanyName());
                statement.executeUpdate();
                java.util.Set<concordia.domain.Item> itemSet = Concordia.get(i).getItems();
                java.util.List<concordia.domain.Item> itemList = new java.util.ArrayList<>(itemSet);
                for (int j = 0; j < itemList.size(); j++) {
                    concordia.domain.Item item = itemList.get(j);
                    statement = (PreparedStatement) con.prepareStatement("INSERT INTO ITEM(ITEM_ID,COMPANY_ID,QUANTITY,ITEMNAME)  VALUES  (?,?,?,?)");
                    statement.setInt(1, item.getItemId());
                    statement.setInt(2, Concordia.get(i).getCompanyId());
                    statement.setInt(3, item.getQuantity());
                    statement.setString(4, item.getItemName());
                    statement.executeUpdate();

                    java.util.List<concordia.domain.History> historyList = item.getHistory();
                    for (int k = 0; k < historyList.size(); k++) {
                        concordia.domain.History history = historyList.get(k);
                        statement = (PreparedStatement) con.prepareStatement("INSERT  INTO  history(HISTORY_ID,ITEM_id,AMOUNT,LOCATION,Supplier,DELIVERY_DATE)  VALUES  (?,?,?,?,?,?)");
                        statement.setInt(1, history.getHistoryId());
                        statement.setInt(2, item.getItemId());
                        statement.setInt(3, history.getAmount());
                        statement.setString(4, history.getLocation());
                        statement.setString(5, history.getSupplier());
                        statement.setString(6, history.getDeliveryDate());
                        statement.executeUpdate();
                    }

                    //backup the users
                    /*  for(int l=0;l<Concordia.get(i).getUsers().size();l++){
                    statement = (PreparedStatement) con.prepareStatement("INSERT INTO USERs(USER_ID,USER_NAME,USER_PASSWORD,COMPANY_ID)  VALUES  (?,?,?,?)"); 
                    statement.setInt(1, Concordia.get(i).getUsers().get(l).getUserId());
                    statement.setString(2, Concordia.get(i).getUsers().get(l).getUsername());
                    statement.setString(3, Concordia.get(i).getUsers().get(l).getPassword());
                    statement.setInt(4, Concordia.get(i).getUsers().get(l).getCompanyId());
                    statement.executeUpdate();
                    
                    }*/
                }

            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
}
