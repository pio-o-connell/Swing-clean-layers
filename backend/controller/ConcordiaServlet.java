package backend.controller;

import backend.dto.CompanyDto;
import backend.dto.HistoryDto;
import backend.dto.ItemDto;
import backend.dto.UserDto;
import backend.infrastructure.JpaContext;
import backend.infrastructure.PersistenceFactory;
import backend.repository.UserRepository;
import backend.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.history;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@WebServlet("/api/concordia")
public class ConcordiaServlet extends HttpServlet {
    private transient EntityManagerFactory entityManagerFactory;
    private ObjectMapper mapper;

    @Override
    public void init() {
        mapper = new ObjectMapper();
        entityManagerFactory = PersistenceFactory.create();
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = normalizeType(req.getParameter("type"));
        if (type == null) {
            respondBadRequest(resp, "Missing type parameter");
            return;
        }
        resp.setContentType("application/json");
        try (JpaContext context = JpaContext.open(entityManagerFactory)) {
            InventoryService inventory = context.inventoryService();
            UserRepository users = context.userRepository();
            switch (type) {
                case "item":
                    writeJson(resp, mapItems(inventory.getAllItems()));
                    break;
                case "history":
                    writeJson(resp, mapHistories(inventory.getAllHistory()));
                    break;
                case "company":
                    writeJson(resp, mapCompanies(inventory.getAllCompanies()));
                    break;
                case "user":
                    writeJson(resp, mapUsers(users.getAllUsers()));
                    break;
                default:
                    respondBadRequest(resp, "GET not supported for type: " + type);
            }
        } catch (Exception ex) {
            respondServerError(resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = normalizeType(req.getParameter("type"));
        if (type == null) {
            respondBadRequest(resp, "Missing type parameter");
            return;
        }
        try (JpaContext context = JpaContext.open(entityManagerFactory)) {
            InventoryService inventory = context.inventoryService();
            UserRepository users = context.userRepository();
            switch (type) {
                case "item": {
                    ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                    inventory.addItem(dto.companyId, dto.quantity, dto.itemName, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("Item created");
                    break;
                }
                case "history": {
                    HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                    inventory.addHistory(dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("History created");
                    break;
                }
                case "company": {
                    CompanyDto dto = mapper.readValue(req.getInputStream(), CompanyDto.class);
                    context.companyRepository().insertCompany(dto.companyId, dto.companyName);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("Company created");
                    break;
                }
                case "user": {
                    UserDto dto = mapper.readValue(req.getInputStream(), UserDto.class);
                    users.insertUser(dto.userId, dto.companyId, dto.username, "");
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("User created");
                    break;
                }
                default:
                    respondBadRequest(resp, "POST not supported for type: " + type);
            }
        } catch (Exception ex) {
            respondServerError(resp, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = normalizeType(req.getParameter("type"));
        if (type == null) {
            respondBadRequest(resp, "Missing type parameter");
            return;
        }
        try (JpaContext context = JpaContext.open(entityManagerFactory)) {
            InventoryService inventory = context.inventoryService();
            UserRepository users = context.userRepository();
            switch (type) {
                case "item": {
                    ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                    inventory.updateItem(dto.itemId, dto.companyId, dto.quantity, dto.itemName, dto.notes);
                    resp.getWriter().write("Item updated");
                    break;
                }
                case "history": {
                    HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                    inventory.updateHistory(dto.historyId, dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes);
                    resp.getWriter().write("History updated");
                    break;
                }
                case "company": {
                    CompanyDto dto = mapper.readValue(req.getInputStream(), CompanyDto.class);
                    Company company = new Company(dto.companyId, "", dto.companyName, new HashSet<>(), new HashSet<>());
                    context.companyRepository().updateCompany(company);
                    resp.getWriter().write("Company updated");
                    break;
                }
                case "user": {
                    UserDto dto = mapper.readValue(req.getInputStream(), UserDto.class);
                    User user = new User(dto.userId, dto.companyId, dto.username, "");
                    users.updateUser(user);
                    resp.getWriter().write("User updated");
                    break;
                }
                default:
                    respondBadRequest(resp, "PUT not supported for type: " + type);
                    return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            respondServerError(resp, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = normalizeType(req.getParameter("type"));
        if (type == null) {
            respondBadRequest(resp, "Missing type parameter");
            return;
        }
        String idParam = req.getParameter("id");
        if (idParam == null) {
            respondBadRequest(resp, "Missing id parameter");
            return;
        }
        int id = Integer.parseInt(idParam);
        try (JpaContext context = JpaContext.open(entityManagerFactory)) {
            InventoryService inventory = context.inventoryService();
            UserRepository users = context.userRepository();
            switch (type) {
                case "item":
                    inventory.deleteItem(id);
                    resp.getWriter().write("Item deleted");
                    break;
                case "history":
                    context.historyRepository().deleteHistory(id);
                    resp.getWriter().write("History deleted");
                    break;
                case "company":
                    context.companyRepository().deleteCompany(id);
                    resp.getWriter().write("Company deleted");
                    break;
                case "user":
                    users.deleteUser(id);
                    resp.getWriter().write("User deleted");
                    break;
                default:
                    respondBadRequest(resp, "DELETE not supported for type: " + type);
                    return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            respondServerError(resp, ex);
        }
    }

    private List<CompanyDto> mapCompanies(Collection<Company> companies) {
        List<CompanyDto> result = new ArrayList<>();
        if (companies == null) {
            return result;
        }
        for (Company company : companies) {
            CompanyDto dto = new CompanyDto();
            dto.companyId = company.getCompanyId();
            dto.companyName = company.getCompanyName();
            dto.items = mapItems(company.getItems());
            dto.users = mapUsers(company.getUsers());
            result.add(dto);
        }
        return result;
    }

    private List<ItemDto> mapItems(Collection<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        if (items == null) {
            return result;
        }
        for (Item item : items) {
            ItemDto dto = new ItemDto();
            dto.itemId = item.getItemId();
            dto.companyId = item.getCompanyId();
            dto.quantity = item.getQuantity();
            dto.itemName = item.getItemName();
            dto.notes = item.getNotes();
            result.add(dto);
        }
        return result;
    }

    private List<HistoryDto> mapHistories(Collection<history> histories) {
        List<HistoryDto> result = new ArrayList<>();
        if (histories == null) {
            return result;
        }
        for (history record : histories) {
            HistoryDto dto = new HistoryDto();
            dto.historyId = record.getHistoryId();
            dto.itemId = record.getItemId();
            dto.amount = record.getAmount();
            dto.location = record.getLocation();
            dto.provider = record.getProvider();
            dto.deliveryDate = record.getDeliveryDate();
            dto.notes = record.getNotes();
            result.add(dto);
        }
        return result;
    }

    private List<UserDto> mapUsers(Collection<User> users) {
        List<UserDto> result = new ArrayList<>();
        if (users == null) {
            return result;
        }
        for (User user : users) {
            UserDto dto = new UserDto();
            dto.userId = user.getUserId();
            dto.companyId = user.getCompanyId();
            dto.username = user.getUsername();
            result.add(dto);
        }
        return result;
    }

    private void writeJson(HttpServletResponse resp, Object payload) throws IOException {
        resp.getWriter().write(mapper.writeValueAsString(payload));
    }

    private void respondBadRequest(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(message);
    }

    private void respondServerError(HttpServletResponse resp, Exception ex) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write("Error: " + ex.getMessage());
    }

    private String normalizeType(String type) {
        return type == null ? null : type.trim().toLowerCase();
    }
}
