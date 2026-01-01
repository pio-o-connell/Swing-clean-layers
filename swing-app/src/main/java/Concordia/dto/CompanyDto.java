package concordia.dto;

import java.util.List;
import concordia.dto.ItemDto;
import concordia.dto.UserDto;

public class CompanyDto {
    public int companyId;
    public String companyName;
    public List<ItemDto> items;
    public List<UserDto> users;
}
