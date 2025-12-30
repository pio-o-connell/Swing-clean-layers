
package Concordia.service;
import Concordia.annotations.Service;

import Concordia.repository.RestoreRepository;
import Concordia.domain.Company;
import java.sql.SQLException;
import java.util.List;

@Service
public class RestoreService {
    private final RestoreRepository repo;
    public RestoreService(RestoreRepository repo) {
        this.repo = repo;
    }
    public void restore(List<Company> companies) throws SQLException {
        repo.setup(new java.util.ArrayList<>(companies));
    }
}
