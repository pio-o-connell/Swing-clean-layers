package Concordia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Script to reset the database and add a new company with 4 gardening-themed items and history.
 */
public class AddGardeningTestData {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://127.0.0.1:5432/concordia";
        String user = "postgres";
        String password = "password";
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        Random rand = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Reset all data
        stmt.executeUpdate("DELETE FROM history");
        stmt.executeUpdate("DELETE FROM item");
        stmt.executeUpdate("DELETE FROM company");

        // Add companies (existing + new gardening company)
        String[] companyNames = {"Kanturk-Services", "Mallow-Services", "Cork-Enterprises", "GreenThumb Supplies"};
        int[] companyIds = {44008177, 55008177, 66008177, 77008177};
        for (int i = 0; i < companyNames.length; i++) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO company (company_id, company_name) VALUES (?, ?)");
            ps.setInt(1, companyIds[i]);
            ps.setString(2, companyNames[i]);
            ps.executeUpdate();
            ps.close();
        }

        // Add items for the new gardening company
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

            // Add 3 history records per item
            String[] locations = {"Greenhouse", "Garden Shed", "Nursery", "Compost Area"};
            String[] providers = {"GardenWorld", "PlantDepot", "SeedMasters", "ToolTown"};
            String[] notes = {
                "Planted in spring for best results",
                "Keep soil moist and well-drained",
                "Fertilize every two weeks"
            };
            for (int h = 0; h < 3; h++) {
                int historyId = 20000000 + j * 10 + h;
                int amount = 5 + rand.nextInt(20);
                String location = locations[rand.nextInt(locations.length)];
                String provider = providers[rand.nextInt(providers.length)];
                String deliveryDate = sdf.format(new Date(System.currentTimeMillis() - rand.nextInt(1000 * 60 * 60 * 24 * 365)));
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

        con.close();
        System.out.println("Database reset and new gardening company/items/history added.");
    }
}
