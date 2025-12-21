package WareHouse.repository;

import WareHouse.domain.Company;
import java.io.*;
import java.util.ArrayList;

public class FileBackupRepository {
    public void saveCompanies(ArrayList<Company> companies) {
        try {
            if (companies != null) {
                FileOutputStream fileStream = new FileOutputStream("Company.ser");
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(companies);
                os.close();
                fileStream.close();
            } else {
                System.out.println("Null Object");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Company> restoreCompanies() {
        ArrayList<Company> companies = null;
        try {
            FileInputStream fileStream = new FileInputStream("Company.ser");
            ObjectInputStream os = new ObjectInputStream(fileStream);
            companies = (ArrayList<Company>) os.readObject();
            System.out.println("Location" + companies.get(0).getItems().get(0).getHistory().get(0).getLocation());
            os.close();
            fileStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return companies;
    }
}
