package Concordia.domain;

import java.io.Serializable;
import java.util.Date;

public class history implements Serializable{
	public String getSupplier() { return provider; }
	public void setSupplier(String supplier) { setProvider(supplier); }
	private int historyId, itemId, amount;
	private String location;
	private String provider;
	private String deliveryDate;
	private String notes;
	public history(int historyId, int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
		this.historyId = historyId;
		this.itemId = itemId;
		this.amount = amount;
		this.location = location;
		this.provider = provider;
		this.deliveryDate = deliveryDate;
		this.notes = notes;
	}
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	public history(){}
	public int getHistoryId() { return historyId; }
	public void setHistoryId(int historyId) { this.historyId = historyId; }
	public int getItemId() { return itemId; }
	public void setItemId(int itemId) { this.itemId = itemId; }
	public int getAmount() { return amount; }
	public void setAmount(int amount) { this.amount = amount; }
	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }
	public String getProvider() { return provider; }
	public void setProvider(String provider) { this.provider = provider; }
	public String getDeliveryDate() { return deliveryDate; }
	public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
}
