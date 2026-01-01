
package concordia.controller;
import concordia.annotations.Controller;

import concordia.service.RestoreService;

@Controller
public class RestoreController {
    private final RestoreService service;
    public RestoreController(RestoreService service) {
        this.service = service;
    }
    public void restorePressed(java.util.List<concordia.domain.Company> companies) throws java.sql.SQLException {
        service.restore(companies);
    }
}
