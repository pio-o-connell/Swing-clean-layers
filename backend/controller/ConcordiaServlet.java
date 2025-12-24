    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        try {
            switch (type) {
                case "item": {
                    ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                    itemRepo.insertNewItem(dto.companyId, dto.quantity, dto.itemName, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("Item created");
                    break;
                }
                case "history": {
                    HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                    historyRepo.insertHistory(dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("History created");
                    break;
                }
                // Company and User creation would require additional repository methods
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("POST not supported for type: " + type);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        try {
            switch (type) {
                case "item": {
                    ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                    itemRepo.deleteItem(dto.itemId);
                    itemRepo.insertNewItem(dto.companyId, dto.quantity, dto.itemName, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Item updated");
                    break;
                }
                case "history": {
                    HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                    historyRepo.updateHistory(new history(dto.historyId, dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes));
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("History updated");
                    break;
                }
                // Company and User update would require additional repository methods
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("PUT not supported for type: " + type);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        try {
            switch (type) {
                case "item": {
                    int itemId = Integer.parseInt(req.getParameter("id"));
                    itemRepo.deleteItem(itemId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Item deleted");
                    break;
                }
                case "history": {
                    int historyId = Integer.parseInt(req.getParameter("id"));
                    historyRepo.deleteHistory(historyId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("History deleted");
                    break;
                }
                // Company and User delete would require additional repository methods
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("DELETE not supported for type: " + type);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
package backend.controller;

import backend.dto.*;
import WareHouse.repository.CompanyRepository;
import WareHouse.repository.ItemRepository;
import WareHouse.repository.HistoryRepository;
import WareHouse.repository.UserRepository;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.history;
import WareHouse.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/api/concordia")
public class ConcordiaServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String type = req.getParameter("type");
            resp.setContentType("application/json");
            try {
                switch (type) {
                    case "item": {
                        List<Item> items = itemRepo.getAllItems();
                        List<ItemDto> dtos = mapItems(items);
                        resp.getWriter().write(mapper.writeValueAsString(dtos));
                        break;
                    }
                    case "history": {
                        List<history> histories = historyRepo.getAllHistory();
                        List<HistoryDto> dtos = mapHistories(histories);
                        resp.getWriter().write(mapper.writeValueAsString(dtos));
                        break;
                    }
                    case "company": {
                        List<Company> companies = companyRepo.getAllCompanies();
                        List<CompanyDto> dtos = mapCompanies(companies);
                        resp.getWriter().write(mapper.writeValueAsString(dtos));
                        break;
                    }
                    case "user": {
                        List<User> users = userRepo.getAllUsers();
                        List<UserDto> dtos = mapUsers(users);
                        resp.getWriter().write(mapper.writeValueAsString(dtos));
                        break;
                    }
                    default:
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("GET not supported for type: " + type);
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error: " + e.getMessage());
            }
        }
    private ObjectMapper mapper;
    private CompanyRepository companyRepo;
    private ItemRepository itemRepo;
    private HistoryRepository historyRepo;
    private UserRepository userRepo;

    @Override
    public void init() {
        try {
            // Adjust connection string, user, password as needed
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/WareHouse", "root", "password");
            companyRepo = new CompanyRepository(con);
            itemRepo = new ItemRepository(con);
            historyRepo = new HistoryRepository(con);
            userRepo = new UserRepository(con);
            mapper = new ObjectMapper();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        try {
            switch (type) {
                case "item": {
                    ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                    itemRepo.insertNewItem(dto.companyId, dto.quantity, dto.itemName, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("Item created");
                    break;
                }
                case "history": {
                    HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                    historyRepo.insertHistory(dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("History created");
                    break;
                }
                case "company": {
                    CompanyDto dto = mapper.readValue(req.getInputStream(), CompanyDto.class);
                    companyRepo.insertCompany(dto.companyId, dto.companyName);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("Company created");
                    break;
                }
                case "user": {
                    UserDto dto = mapper.readValue(req.getInputStream(), UserDto.class);
                    userRepo.insertUser(dto.userId, dto.companyId, dto.username, ""); // Password handling TBD
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("User created");
                    break;
                }
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("POST not supported for type: " + type);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    // Mapping methods
    private List<CompanyDto> mapCompanies(List<Company> companies) {
        List<CompanyDto> dtos = new ArrayList<>();
        for (Company c : companies) {
            CompanyDto dto = new CompanyDto();
            dto.companyId = c.getCompanyId();
            dto.companyName = c.getCompanyName();
            dto.items = mapItems(c.getItems());
            dto.users = mapUsers(c.getUsers());
            dtos.add(dto);
        }
        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String type = req.getParameter("type");
            try {
                switch (type) {
                    case "item": {
                        ItemDto dto = mapper.readValue(req.getInputStream(), ItemDto.class);
                        itemRepo.deleteItem(dto.itemId);
                        itemRepo.insertNewItem(dto.companyId, dto.quantity, dto.itemName, dto.notes);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("Item updated");
                        break;
                    }
                    case "history": {
                        HistoryDto dto = mapper.readValue(req.getInputStream(), HistoryDto.class);
                        historyRepo.updateHistory(new history(dto.historyId, dto.itemId, dto.amount, dto.location, dto.provider, dto.deliveryDate, dto.notes));
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("History updated");
                        break;
                    }
                    case "company": {
                        CompanyDto dto = mapper.readValue(req.getInputStream(), CompanyDto.class);
                        companyRepo.updateCompany(new WareHouse.domain.Company(dto.companyId, dto.companyName, new ArrayList<>(), new ArrayList<>()));
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("Company updated");
                        break;
                    }
                    case "user": {
                        UserDto dto = mapper.readValue(req.getInputStream(), UserDto.class);
                        userRepo.updateUser(new WareHouse.domain.User(dto.userId, dto.companyId, dto.username, "")); // Password handling TBD
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("User updated");
                        break;
                    }
                    default:
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("PUT not supported for type: " + type);
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error: " + e.getMessage());
            }
        }
        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String type = req.getParameter("type");
            try {
                switch (type) {
                    case "item": {
                        int itemId = Integer.parseInt(req.getParameter("id"));
                        itemRepo.deleteItem(itemId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("Item deleted");
                        break;
                    }
                    case "history": {
                        int historyId = Integer.parseInt(req.getParameter("id"));
                        historyRepo.deleteHistory(historyId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("History deleted");
                        break;
                    }
                    case "company": {
                        int companyId = Integer.parseInt(req.getParameter("id"));
                        companyRepo.deleteCompany(companyId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("Company deleted");
                        break;
                    }
                    case "user": {
                        int userId = Integer.parseInt(req.getParameter("id"));
                        userRepo.deleteUser(userId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("User deleted");
                        break;
                    }
                    default:
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("DELETE not supported for type: " + type);
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error: " + e.getMessage());
            }
        }
