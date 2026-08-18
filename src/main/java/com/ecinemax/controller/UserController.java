package com.ecinemax.controller;

import com.ecinemax.dto.ChangePasswordRequest;
import com.ecinemax.dto.UpdateProfileRequest;
import com.ecinemax.dto.UserProfileDto;
import com.ecinemax.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserProfileDto getProfile(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    @PutMapping
    public UserProfileDto updateProfile(@RequestBody UpdateProfileRequest request, Authentication authentication) {
        return userService.updateProfile(authentication.getName(), request);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        userService.changePassword(authentication.getName(), request);
    }
}
