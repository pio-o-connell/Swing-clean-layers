package backend.infrastructure;

import backend.repository.CompanyRepository;
import backend.repository.HistoryRepository;
import backend.repository.ItemRepository;
import backend.repository.UserRepository;
import backend.service.InventoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Lightweight holder that wires an {@link EntityManager} together with
 * the repositories and services that depend on it. Open a context per
 * request and close it when finished to avoid sharing entity managers
 * across servlet threads.
 */
public final class JpaContext implements AutoCloseable {
    private final EntityManager entityManager;
    private final CompanyRepository companyRepository;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    private JpaContext(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.companyRepository = new CompanyRepository(entityManager);
        this.itemRepository = new ItemRepository(entityManager);
        this.historyRepository = new HistoryRepository(entityManager);
        this.userRepository = new UserRepository(entityManager);
        this.inventoryService = new InventoryService(companyRepository, itemRepository, historyRepository);
    }

    public static JpaContext open(EntityManagerFactory entityManagerFactory) {
        return new JpaContext(entityManagerFactory);
    }

    public InventoryService inventoryService() {
        return inventoryService;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public CompanyRepository companyRepository() {
        return companyRepository;
    }

    public ItemRepository itemRepository() {
        return itemRepository;
    }

    public HistoryRepository historyRepository() {
        return historyRepository;
    }

    @Override
    public void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
