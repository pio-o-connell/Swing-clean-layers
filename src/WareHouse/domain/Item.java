package WareHouse.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/*---------------------------------------------------------------------------------------
 * The 'Item' class is used to hold the particular item in stock by particular manufacturer
 * together with link to historical data associated with past transactions by particular suppliers
 ---------------------------------------------------------------------------------------*/

public class Item implements Serializable {
	private int itemId,companyId,quantity,historyId;
	private String itemName;
	private String notes; // Notes field
	private ArrayList<history> historyItem = new ArrayList<history>();
	Date date = new Date();

	// Updated constructor: removed Location parameter, added Notes
	public Item(int itemId, int companyId, int quantity, String itemName, String notes, ArrayList<history> historyItem) {
		this.itemId = itemId;
		this.companyId = companyId;
		this.quantity = quantity;
		this.itemName = itemName;
		this.notes = notes;
		this.historyItem = historyItem;
	}
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Item(){
	}

	public ArrayList<history> getHistoryItem() {
		return historyItem;
	}

	public void setHistoryItem(ArrayList<history> historyItem) {
		this.historyItem = historyItem;
	}
	// Removed updateItem method to keep domain layer pure

	public void getDelivery(){
		// ...
	}
}
