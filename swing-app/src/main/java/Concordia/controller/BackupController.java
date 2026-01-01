
package concordia.controller;
import concordia.annotations.Controller;

import concordia.service.BackupService;

@Controller
public class BackupController {
    private final BackupService service;
    public BackupController(BackupService service) {
        this.service = service;
    }
    public void backupPressed(java.util.List<concordia.domain.Company> companies) throws java.sql.SQLException {
        service.backup(companies);
    }
}
