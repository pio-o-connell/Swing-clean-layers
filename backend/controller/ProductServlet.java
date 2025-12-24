package backend.controller;

import backend.dto.ProductDto;
import backend.service.ProductService;
import backend.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {
    private ProductService service;
    private ObjectMapper mapper;

    @Override
    public void init() {
        DataSource ds = DataSourceFactory.create();
        ProductRepository repo = new ProductRepository(ds);
        service = new ProductService(repo);
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Product> products = service.getAllProducts();
        List<ProductDto> dtoList = products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.id = p.getId();
            dto.name = p.getName();
            dto.stock = p.getStock();
            return dto;
        }).collect(Collectors.toList());
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(resp.getOutputStream(), dtoList);
    }
}
