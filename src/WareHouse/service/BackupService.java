
package WareHouse.service;
import WareHouse.annotations.Service;

import WareHouse.repository.BackupRepository;
import WareHouse.domain.Company;
import java.sql.SQLException;
import java.util.List;

@Service
public class BackupService {
    private final BackupRepository repo;
    public BackupService(BackupRepository repo) {
        this.repo = repo;
    }
    public void backup(List<Company> companies) throws SQLException {
        repo.backup(new java.util.ArrayList<>(companies));
    }
}
