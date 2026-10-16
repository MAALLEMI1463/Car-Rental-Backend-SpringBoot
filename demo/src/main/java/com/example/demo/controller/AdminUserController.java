package com.example.demo.controller;

import com.example.demo.dto.AdminCreateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.InputSanitizer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here are restricted to ADMIN both by SecurityConfig
 * (/api/admin/** -> hasRole("ADMIN")) and, defensively, by @PreAuthorize.
 */
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AdminUserController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody AdminCreateUserRequest request) {
        UserResponse created = authService.createUserByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Real-time client search for the "book on behalf of a client" flow
     * (Task 4). Matches on username or phone; only ever returns CLIENT
     * accounts. The keyword is sanitized the same way car search is.
     */
    @GetMapping("/search-clients")
    public ResponseEntity<List<UserResponse>> searchClients(@RequestParam String keyword) {
        String cleanKeyword = InputSanitizer.sanitizeSearchKeyword(keyword);
        if (cleanKeyword == null) {
            return ResponseEntity.ok(List.of());
        }
        List<UserResponse> users = userRepository.searchClients(cleanKeyword, Role.CLIENT).stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(users);
    }
}