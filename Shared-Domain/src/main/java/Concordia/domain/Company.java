package Concordia.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Company implements Serializable{
	public String getName() { return getCompanyName(); }
	public int getCompanyId() { return companyId; }
	public String getCompanyName() { return companyName; }
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
	public ArrayList<Item> getItems() { return Items; }
	public void setItems(ArrayList<Item> items) { Items = items; }
	public ArrayList<User> getUsers() { return Users; }
	public void setUsers(ArrayList<User> users) { Users = users; }
	public void addItem(){ this.Items.add(new Item()); }
}
