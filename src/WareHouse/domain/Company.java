package WareHouse.domain;

import java.io.Serializable;
import java.util.ArrayList;

/*---------------------------------------------------------------------------------------
 * The 'Company' class holds the manufacturers companies name and unique id, there are links to the Users
 * of the system and the particular items stocked by this company(from the Manufacturer) together with historical
 * references to past transactions by particular suppliers
 ---------------------------------------------------------------------------------------*/

public class Company implements Serializable{
	// Compatibility method for legacy code
	public String getName() {
		return getCompanyName();
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	private int companyId;
	private String companyName;
	private ArrayList<Item> Items = new ArrayList<Item>();
	private ArrayList<User> Users = new ArrayList<User>();

	public Company(int companyId,String companyName,ArrayList<Item> items,ArrayList<User> users){
		this.companyId = companyId;
		this.companyName=companyName;
		this.Items=items;
		this.Users=users;
	}

	public ArrayList<Item> getItems() {
		return Items;
	}
	public void setItems(ArrayList<Item> items) {
		Items = items;
	}
	public ArrayList<User> getUsers() {
		return Users;
	}
	public void setUsers(ArrayList<User> users) {
		Users = users;
	}

	public void addItem(){
		this.Items.add(new Item());
	}
	// Removed updateItem method to keep domain layer pure
}
