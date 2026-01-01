
package concordia.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item implements Serializable {

	@Id
	@Column(name = "item_id")
	private int itemId;

	@Column(name = "company_id")
	private int companyId;

	@ManyToOne
	@JoinColumn(name = "company_id", insertable = false, updatable = false)
	private Company company;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "item_name", length = 25)
	private String itemName;

	@Column(name = "location", length = 25)
	private String location;

	@Column(name = "notes", length = 200)
	private String notes;

	@Column(name = "date")
	private java.sql.Timestamp date;

	@OneToMany(mappedBy = "item")
	private List<History> historyItem = new ArrayList<>();

	// Legacy no-arg constructor for ORM/compatibility
	public Item() {}

	// Legacy constructor for compatibility (without List<History>)
	public Item(int itemId, Company company, int quantity, String itemName, String location, String notes, java.sql.Timestamp date) {
		this.itemId = itemId;
		this.company = company;
		this.quantity = quantity;
		this.itemName = itemName;
		this.location = location;
		this.notes = notes;
		this.date = date;
		this.historyItem = new ArrayList<>();
	}

	// Legacy getter/setter for company (if not present)
	public Company getCompany() { return company; }
	public void setCompany(Company company) { this.company = company; }

	// Legacy getter/setter for history (if not present)
	public List<History> getHistory() { return historyItem; }
	public void setHistory(List<History> history) { this.historyItem = history; }

	// Legacy getter/setter for supplier (dummy for compatibility)
	public String getSupplier() { return null; }
	public void setSupplier(String supplier) { /* no-op for compatibility */ }
	// ORM constructor
	public Item(int itemId, Company company, int quantity, String itemName, String location, String notes, java.sql.Timestamp date, List<History> historyItem) {
		this.itemId = itemId;
		this.company = company;
		this.quantity = quantity;
		this.itemName = itemName;
		this.location = location;
		this.notes = notes;
		this.date = date;
		this.historyItem = historyItem;
	}

	// Getters and setters
	public int getItemId() { return itemId; }
	public void setItemId(int itemId) { this.itemId = itemId; }

	public int getCompanyId() { return company != null ? company.getCompanyId() : companyId; }
	public void setCompanyId(int companyId) { this.companyId = companyId; }


	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }

	public String getItemName() { return itemName; }
	public void setItemName(String itemName) { this.itemName = itemName; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	public java.sql.Timestamp getDate() { return date; }
	public void setDate(java.sql.Timestamp date) { this.date = date; }

	public List<History> getHistoryItem() { return historyItem; }
	public void setHistoryItem(List<History> historyItem) { this.historyItem = historyItem; }

}







