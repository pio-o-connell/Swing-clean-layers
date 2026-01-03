
package concordia.controller;

import java.sql.SQLException;
import java.util.List;

import concordia.annotations.Controller;
import concordia.domain.Company;
import concordia.service.BackupService;

@Controller
public class BackupController {
    private final BackupService service;
    public BackupController(BackupService service) {
        this.service = service;
    }

    public void backupPressed(List<Company> companies) throws SQLException {
        service.backup(companies);
    }
}
