
package Concordia.controller;
import Concordia.annotations.Controller;

import Concordia.service.BackupService;

@Controller
public class BackupController {
    private final BackupService service;
    public BackupController(BackupService service) {
        this.service = service;
    }
    public void backupPressed(java.util.List<Concordia.domain.Company> companies) throws java.sql.SQLException {
        service.backup(companies);
    }
}
