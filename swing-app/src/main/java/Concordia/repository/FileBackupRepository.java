
package concordia.repository;
import concordia.annotations.Repository;

import concordia.domain.Company;
import java.io.*;
import java.util.ArrayList;

@Repository
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
            java.util.Set<concordia.domain.Item> itemSet = companies.get(0).getItems();
            java.util.List<concordia.domain.Item> itemList = new java.util.ArrayList<>(itemSet);
            java.util.List<concordia.domain.History> historyList = itemList.isEmpty() ? new java.util.ArrayList<>() : itemList.get(0).getHistory();
            String location = historyList.isEmpty() ? null : historyList.get(0).getLocation();
            System.out.println("Location" + location);
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
