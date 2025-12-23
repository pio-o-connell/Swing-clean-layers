
package WareHouse.controller;
import WareHouse.annotations.Controller;

import WareHouse.service.RestoreService;

@Controller
public class RestoreController {
    private final RestoreService service;
    public RestoreController(RestoreService service) {
        this.service = service;
    }
    public void restorePressed(java.util.List<WareHouse.domain.Company> companies) throws java.sql.SQLException {
        service.restore(companies);
    }
}
