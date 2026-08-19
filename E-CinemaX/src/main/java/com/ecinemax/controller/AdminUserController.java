package com.ecinemax.controller;

import com.ecinemax.dto.AdminUserDto;
import com.ecinemax.dto.UpdateUserStatusRequest;
import com.ecinemax.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<AdminUserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody UpdateUserStatusRequest request) {
        userService.setUserEnabled(id, request);
    }
}
