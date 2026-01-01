package concordia;

//----------------------------------------------------------------------------//
// Generate report interrogates the memory and generates a report
// Implement 'Report' functionality' in main window
// Launches Notepad with the results - 
// An enhancement would be to generate a HTML file with the results displayed in tabular form
//
// Note - ItemReport for date not implemented.
//-------------------------------------------------------------------------------------//
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;
// import concordia.domain.Index; // Removed: Index class does not exist
import java.util.Date;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;

public class GenerateReport {

    // Removed static/global Company. Use dependency injection via constructor or method parameter.
    
    Date dateFrom;
    Date dateTo;

    GenerateReport(ArrayList<Company> companyList) throws IOException{
        File outputFile = new File("Report-JavaProject.txt");
        FileWriter out = new FileWriter(outputFile);
        PrintWriter outputStream = new PrintWriter(out);
        for(int i=0;i<companyList.size();i++){
            Date date = new Date();
            outputStream.println("Date"+date.toString());
            outputStream.print("Company Name: "+companyList.get(i).getCompanyName());
            outputStream.println("\tCompany Id: "+companyList.get(i).getCompanyId());
            for(int j=0;j<companyList.get(i).getItems().size();j++){
                java.util.Set<concordia.domain.Item> itemSet = companyList.get(i).getItems();
                java.util.List<concordia.domain.Item> itemList = new java.util.ArrayList<>(itemSet);
                if (j >= itemList.size()) continue;
                concordia.domain.Item item = itemList.get(j);
                outputStream.println("Item Name: \t\t\tItem Id: \t\t Item Quantity: \t\tItem Location: ");
                outputStream.println(item.getItemName()+"\t\t\t " +item.getItemId()+"\t\t\t" +item.getQuantity());
                outputStream.println("\t\t\tHistory Location: \t\t\tHistoryId:  \t\t Amount: \t\tSupplier: \t\tDelivery Date: ");
                java.util.List<concordia.domain.History> historyList = item.getHistory();
                for(int k=0;k<historyList.size();k++){
                    concordia.domain.History history = historyList.get(k);
                    outputStream.println("\t\t\t "+ history.getLocation()+"\t\t\t "+ history.getHistoryId()+"\t\t"+history.getAmount()+"\t\t  "+ history.getSupplier()+"\t\t "+history.getDeliveryDate());
                }
            }
        }
        out.close();
    }

    // Removed GenerateItemsDatesReport method: relied on Index class which does not exist

}