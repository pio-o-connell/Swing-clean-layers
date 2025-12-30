import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;

import Concordia.annotations.Configuration;

@Configuration
public class GenerateDummyHistoryData {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/concordia?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "ROOT";
        Connection con = DriverManager.getConnection(url, user, password);

        int numRecords = 100; // Change as needed
        Random rand = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] locations = {"Dublin", "Cork", "Limerick", "Galway", "Waterford"};
        String[] providers = {"Sony", "Samsung", "LG", "Panasonic", "Philips"};

        for (int i = 0; i < numRecords; i++) {
            int itemId = 44008177 + rand.nextInt(3); // Example item IDs
            int amount = 1 + rand.nextInt(100);
            String location = locations[rand.nextInt(locations.length)];
            String provider = providers[rand.nextInt(providers.length)];
            String deliveryDate = sdf.format(new Date(System.currentTimeMillis() - rand.nextInt(1000 * 60 * 60 * 24 * 365)));

            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO history (ITEM_id, AMOUNT, LOCATION, PROVIDER, DELIVERY_DATE) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, itemId);
            stmt.setInt(2, amount);
            stmt.setString(3, location);
            stmt.setString(4, provider);
            stmt.setString(5, deliveryDate);
            stmt.executeUpdate();
            stmt.close();
        }
        con.close();
        System.out.println("Dummy history data generated.");
    }
}
