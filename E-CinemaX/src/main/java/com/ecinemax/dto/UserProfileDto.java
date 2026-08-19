package com.ecinemax.dto;

import java.time.LocalDate;

// Fuller than UserDto (which is just the auth response) - includes the
// fields Useraccount.html/Editprofile.html actually display and edit.
public class UserProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    public UserProfileDto() {
    }

    public UserProfileDto(Long id, String firstName, String lastName, String email, String phone, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
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

    public String getPhone() {
        return phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
