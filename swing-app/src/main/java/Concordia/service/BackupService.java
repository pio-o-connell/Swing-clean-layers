
package Concordia.service;
import Concordia.annotations.Service;

import Concordia.repository.BackupRepository;
import Concordia.domain.Company;
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
