package com.project.nexushub.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.nexushub.dto.AddProductRequest;
import com.project.nexushub.dto.DeleteUserRequest;
import com.project.nexushub.dto.AdminResponse;
import com.project.nexushub.dto.UpdateUserRequest;
import com.project.nexushub.entity.RoleType;
import com.project.nexushub.entity.User;
import com.project.nexushub.repository.RoleRepository;
import com.project.nexushub.repository.UserRepository;
import com.project.nexushub.service.AdminService;
import com.project.nexushub.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @GetMapping("/admin/user")
    public List<AdminResponse> getAllUsers() {

        return adminService.getAllUsers();
    }

    @GetMapping("/admin/find-user/{email}")
    public ResponseEntity<AdminResponse> getUserByEmail(@PathVariable String email) {
        try {
            return adminService.getUserDetailByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<UpdateUserRequest> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            return adminService.updatingUser(userId, updateUserRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        try {
            adminService.deletingUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }
}
