package com.ecinemax.service;

import com.ecinemax.dto.RegisterRequest;
import com.ecinemax.dto.UserDto;
import com.ecinemax.entity.AppUser;
import com.ecinemax.entity.UserRole;
import com.ecinemax.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        // Public registration always creates a CUSTOMER - there is no
        // self-service way to become an ADMIN.
        AppUser user = new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone(),
                request.getDateOfBirth(),
                UserRole.CUSTOMER
        );

        userRepository.save(user);
        return toDto(user);
    }

    public static UserDto toDto(AppUser user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }
}
