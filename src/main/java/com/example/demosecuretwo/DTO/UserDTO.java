package com.example.demosecuretwo.DTO;

public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private Double price;
    private String role; // using String for simplicity; you can also use Users.Role

    // Default constructor
    public UserDTO() {}

    // Parameterized constructor
    public UserDTO(Integer id, String username, String email, Double price, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.price = price;
        this.role = role;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

