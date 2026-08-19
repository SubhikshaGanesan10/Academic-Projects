package com.ecinemax.dto;

import com.ecinemax.entity.UserRole;

// What we send back to the browser after register/login, and from /api/auth/me.
// Never includes the password hash.
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public UserDto() {
    }

    public UserDto(Long id, String firstName, String lastName, String email, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}
