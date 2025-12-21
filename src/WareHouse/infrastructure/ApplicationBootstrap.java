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
            ItemRepository itemRepo = new ItemRepository(con);
            InventoryService service = new InventoryService(itemRepo);
            InventoryController controller = new InventoryController(service);
            new Mainframe("Warehouse Inventory System", controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
