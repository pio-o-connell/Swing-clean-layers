package concordia;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import concordia.repository.CompanyRepository;
import concordia.repository.ItemRepository;
import concordia.repository.HistoryRepository;
import concordia.service.InventoryService;
import concordia.controller.InventoryController;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;
import javax.swing.SwingUtilities;

public class ORMLauncher {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("concordiaPU");
            em = emf.createEntityManager();
            // Wire up repositories, service, controller
            CompanyRepository companyRepo = new CompanyRepository(em);
            ItemRepository itemRepo = new ItemRepository(em);
            HistoryRepository historyRepo = new HistoryRepository(em);
            InventoryService service = new InventoryService(companyRepo, itemRepo, historyRepo);
            InventoryController controller = new InventoryController(service);
            // Launch the main UI (Mainframe) with the controller
            SwingUtilities.invokeLater(() -> {
                Mainframe frame = new Mainframe("Concordia ORM App", controller);
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Do not close em/emf here, keep open for app lifetime
        }
    }
}
