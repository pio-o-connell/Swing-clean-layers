package Concordia;

import Concordia.domain.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class InitDatabase {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://127.0.0.1:5432/concordia";
        String user = "postgres";
        String password = "password";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            ArrayList<Company> companyList = new ArrayList<>();
            Databases db = new Databases(con);
            db.init(con, companyList);
            con.close();
            System.out.println("Database tables created and populated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
