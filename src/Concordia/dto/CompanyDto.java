package Concordia.dto;

import java.util.List;
import Concordia.dto.ItemDto;
import Concordia.dto.UserDto;

public class CompanyDto {
    public int companyId;
    public String companyName;
    public List<ItemDto> items;
    public List<UserDto> users;
}
