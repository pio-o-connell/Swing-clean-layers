
package concordia.service;
import concordia.annotations.Service;

import concordia.repository.RestoreRepository;
import concordia.domain.Company;
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
