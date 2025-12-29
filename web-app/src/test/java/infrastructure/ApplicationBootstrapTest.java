package infrastructure;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationBootstrapTest {
    @Test
    void testDatabaseConnection() {
        String url = "jdbc:postgresql://localhost:5432/concordia";
        String user = "postgres";
        String password = "password";
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            assertNotNull(con, "Connection should not be null");
            assertFalse(con.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}
