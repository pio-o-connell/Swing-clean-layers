
package Concordia.controller;
import Concordia.annotations.Controller;

import Concordia.service.RestoreService;

@Controller
public class RestoreController {
    private final RestoreService service;
    public RestoreController(RestoreService service) {
        this.service = service;
    }
    public void restorePressed(java.util.List<Concordia.domain.Company> companies) throws java.sql.SQLException {
        service.restore(companies);
    }
}
