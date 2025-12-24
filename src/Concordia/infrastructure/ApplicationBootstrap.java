
package Concordia.infrastructure;
import Concordia.annotations.Component;
import Concordia.annotations.Configuration;

import Concordia.repository.ItemRepository;
import Concordia.service.InventoryService;
import Concordia.controller.InventoryController;
import Concordia.Mainframe;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
@Configuration
public class ApplicationBootstrap {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/warehouse?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String password = "ROOT";
            Connection con = DriverManager.getConnection(url, user, password);
            Concordia.repository.CompanyRepository companyRepo = new Concordia.repository.CompanyRepository(con);
            Concordia.repository.ItemRepository itemRepo = new Concordia.repository.ItemRepository(con);
            Concordia.repository.HistoryRepository historyRepo = new Concordia.repository.HistoryRepository(con);
            InventoryService service = new InventoryService(companyRepo, itemRepo, historyRepo);
            InventoryController controller = new InventoryController(service);
            Mainframe frame = new Mainframe("Warehouse Inventory System", controller);
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
