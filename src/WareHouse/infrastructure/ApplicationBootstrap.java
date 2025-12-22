package WareHouse.infrastructure;

import WareHouse.repository.ItemRepository;
import WareHouse.service.InventoryService;
import WareHouse.controller.InventoryController;
import WareHouse.Mainframe;
import java.sql.Connection;
import java.sql.DriverManager;

public class ApplicationBootstrap {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/warehouse?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String password = "ROOT";
            Connection con = DriverManager.getConnection(url, user, password);
            WareHouse.repository.CompanyRepository companyRepo = new WareHouse.repository.CompanyRepository(con);
            WareHouse.repository.ItemRepository itemRepo = new WareHouse.repository.ItemRepository(con);
            WareHouse.repository.HistoryRepository historyRepo = new WareHouse.repository.HistoryRepository(con);
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
