package concordia.dto;

public class UserDto {
    public int userId;
    public int companyId;
    public String username;
    // Do not expose password in DTO for security reasons
}
