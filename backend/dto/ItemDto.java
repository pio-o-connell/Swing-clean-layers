package backend.dto;
import java.util.List;

public class ItemDto {
    public int itemId;
    public int companyId;
    public int quantity;
    public String itemName;
    public String notes;
    public List<HistoryDto> historyItem;
}