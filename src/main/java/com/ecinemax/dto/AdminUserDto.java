package com.ecinemax.dto;

import com.ecinemax.entity.UserRole;

public class AdminUserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private boolean enabled;

    public AdminUserDto(Long id, String firstName, String lastName, String email, UserRole role, boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }
}
