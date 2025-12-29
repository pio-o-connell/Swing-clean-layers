package Concordia.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Item implements Serializable {
	public int getItemId() { return itemId; }
	public int getCompanyId() { return companyId; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public String getItemName() { return itemName; }
	public ArrayList<history> getHistory() { return historyItem; }
	private int itemId,companyId,quantity,historyId;
	private String itemName;
	private String notes;
	private ArrayList<history> historyItem = new ArrayList<history>();
	Date date = new Date();
	public Item(int itemId, int companyId, int quantity, String itemName, String notes, ArrayList<history> historyItem) {
		this.itemId = itemId;
		this.companyId = companyId;
		this.quantity = quantity;
		this.itemName = itemName;
		this.notes = notes;
		this.historyItem = historyItem;
	}
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	public Item(){}
}
