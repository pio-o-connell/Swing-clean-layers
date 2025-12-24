package Concordia.controller;

import Concordia.dto.*;
import Concordia.repository.CompanyRepository;
import Concordia.repository.ItemRepository;
import Concordia.repository.HistoryRepository;
import Concordia.repository.UserRepository;
import Concordia.domain.Company;
import Concordia.domain.Item;
import Concordia.domain.history;
import Concordia.domain.User;
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
    private ObjectMapper mapper;
    private CompanyRepository companyRepo;
    private ItemRepository itemRepo;
    private HistoryRepository historyRepo;
    private UserRepository userRepo;

    @Override
    public void init() {
        try {
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
                    List<Company> companies = companyRepo.loadCompaniesWithItemsAndUsers();
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
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("DELETE not supported for type: " + type);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    // Add your mapItems, mapHistories, mapCompanies, mapUsers methods here
    private List<ItemDto> mapItems(List<Item> items) {
        // TODO: Implement actual mapping logic
        return new ArrayList<>();
    }
    private List<HistoryDto> mapHistories(List<history> histories) {
        // TODO: Implement actual mapping logic
        return new ArrayList<>();
    }
    private List<CompanyDto> mapCompanies(List<Company> companies) {
        // TODO: Implement actual mapping logic
        return new ArrayList<>();
    }
    private List<UserDto> mapUsers(List<User> users) {
        // TODO: Implement actual mapping logic
        return new ArrayList<>();
    }
}
