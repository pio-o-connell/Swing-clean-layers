new fields in the history record foeld called Notes  

new field in the item field for Notes 

public Item(int itemId, int companyId, int quantity, String itemName, JTextArea textArea , ArrayList<history> historyItem) {
		this.itemId = itemId;
		this.companyId = companyId;
		this.quantity = quantity;
		this.itemName = itemName;
        this.textArea = textArea;
		this.historyItem = historyItem;
	}

    becomes JTextArea textArea = new JTextArea(5, 20); // 5 rows, 20 columns

public Item(int itemId, int companyId, int quantity, String itemName, JTextArea textArea ,ArrayList<history> historyItem) {
		this.itemId = itemId;
		this.companyId = companyId;
		this.quantity = quantity;
		this.itemName = itemName;
        this.Notes = Notes;
		this.historyItem = historyItem;
	}

    becomes JTextArea textArea = new JTextArea(5, 20); // 5 rows, 20 columns


    The database tables now become

    ALTER TABLE Item ADD COLUMN description VARCHAR(200);

    We need to alter the item and hoistory tables with  ALTER TABLE Item ADD COLUMN Notes VARCHAR(200);

    Change the scripts on init so that extra data random words is added to the Item and history fields 

    reset all databases and start the app


    On init update all code so that the windows  are updated 
