package sg.nus.iss.shoppingcart.model.DTO;

import sg.nus.iss.shoppingcart.enums.Role;

/**
 * @ClassName UserDTO
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/7
 * @Version 1.0
 */

public class UserDTO {
    private Long id;
    private String username;
    private String role;

    // Constructors
    public UserDTO(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

