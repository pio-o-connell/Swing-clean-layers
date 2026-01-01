
package concordia.infrastructure;
import concordia.annotations.Component;
import concordia.annotations.Configuration;

import concordia.repository.ItemRepository;
import concordia.service.InventoryService;
import concordia.controller.InventoryController;
import concordia.Mainframe;
// import java.sql.Connection;
// import java.sql.DriverManager;

@Component
@Configuration
public class ApplicationBootstrap {
    public static void main(String[] args) {
        try {
            jakarta.persistence.EntityManagerFactory emf = jakarta.persistence.Persistence.createEntityManagerFactory("concordiaPU");
            jakarta.persistence.EntityManager em = emf.createEntityManager();
            concordia.repository.CompanyRepository companyRepo = new concordia.repository.CompanyRepository(em);
            concordia.repository.ItemRepository itemRepo = new concordia.repository.ItemRepository(em);
            concordia.repository.HistoryRepository historyRepo = new concordia.repository.HistoryRepository(em);
            InventoryService service = new InventoryService(companyRepo, itemRepo, historyRepo);
            InventoryController controller = new InventoryController(service);
            Mainframe frame = new Mainframe("Concordia Inventory System", controller);
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
