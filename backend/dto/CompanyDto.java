package backend.dto;
import java.util.List;

public class CompanyDto {
    public int companyId;
    public String companyName;
    public List<ItemDto> items;
    public List<UserDto> users;
}