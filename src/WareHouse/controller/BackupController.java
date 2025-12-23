
package WareHouse.controller;
import WareHouse.annotations.Controller;

import WareHouse.service.BackupService;

@Controller
public class BackupController {
    private final BackupService service;
    public BackupController(BackupService service) {
        this.service = service;
    }
    public void backupPressed(java.util.List<WareHouse.domain.Company> companies) throws java.sql.SQLException {
        service.backup(companies);
    }
}
