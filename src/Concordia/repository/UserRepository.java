package Concordia.repository;
import Concordia.annotations.Repository;
import Concordia.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class UserRepository {
    private final Connection con;
    public UserRepository(Connection con) {
        this.con = con;
    }

    public void insertUser(int userId, int companyId, String username, String password) throws SQLException {
        PreparedStatement statement = con.prepareStatement("INSERT INTO users(user_id, company_id, user_name, user_password) VALUES (?, ?, ?, ?)");
        statement.setInt(1, userId);
        statement.setInt(2, companyId);
        statement.setString(3, username);
        statement.setString(4, password);
        statement.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET company_id = ?, user_name = ?, user_password = ? WHERE user_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, user.getCompanyId());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setInt(4, user.getUserId());
        stmt.executeUpdate();
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.execute();
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
        }
        return users;
    }
}
