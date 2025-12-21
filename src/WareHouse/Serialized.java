package WareHouse;

//----------------------------------------------------------------------------
// Serialized implements the 'Save Settings' and 'Restore' buttons
// The objects state are saved to file to be subsequently restored
//
//------------------------------------------------------------------------
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import WareHouse.domain.Company;

public class Serialized {
	private ArrayList<Company> Company;
	
	       public Serialized(ArrayList<Company> Company){
		       this.Company = Company;
		       try {
			       if(Company!=null){
				       FileOutputStream fileStream = new FileOutputStream("Company.ser");
				       ObjectOutputStream os=new ObjectOutputStream(fileStream);
				       os.writeObject(Company);
				       os.close();
				       fileStream.close();
			       }
			       else {
				       System.out.println("Null Object");
			       }
		       } catch (FileNotFoundException e) {
			       e.printStackTrace();
		       } catch (IOException e) {
			       e.printStackTrace();
		       }
	       }

	       public void restoreSerialized(ArrayList<Company> Company) {
		       this.Company = Company;
		       try {
			       FileInputStream fileStream= new FileInputStream("Company.ser");
			       ObjectInputStream os = new ObjectInputStream(fileStream);
			       Company =(ArrayList<Company> )os.readObject();
			       System.out.println("Location"+(String) Company.get(0).getItems().get(0).getHistory().get(0).getLocation());
	
			
			os.close();
			fileStream.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}