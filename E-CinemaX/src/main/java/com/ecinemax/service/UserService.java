package com.ecinemax.service;

import com.ecinemax.dto.AdminUserDto;
import com.ecinemax.dto.ChangePasswordRequest;
import com.ecinemax.dto.UpdateProfileRequest;
import com.ecinemax.dto.UpdateUserStatusRequest;
import com.ecinemax.dto.UserProfileDto;
import com.ecinemax.entity.AppUser;
import com.ecinemax.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileDto getProfile(String email) {
        return toDto(findUser(email));
    }

    public UserProfileDto updateProfile(String email, UpdateProfileRequest request) {
        AppUser user = findUser(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDateOfBirth(request.getDateOfBirth());
        userRepository.save(user);
        return toDto(user);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        AppUser user = findUser(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password cannot be blank");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // Admin: list every customer/admin account, and enable/disable one.
    // Disabling actually blocks login - AppUserDetailsService checks the
    // same 'enabled' flag when building the Spring Security user.
    public List<AdminUserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserDto(user.getId(), user.getFirstName(), user.getLastName(),
                        user.getEmail(), user.getRole(), user.isEnabled()))
                .toList();
    }

    public void setUserEnabled(Long userId, UpdateUserStatusRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
        user.setEnabled(request.isEnabled());
        userRepository.save(user);
    }

    private AppUser findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserProfileDto toDto(AppUser user) {
        return new UserProfileDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhone(), user.getDateOfBirth());
    }
}
