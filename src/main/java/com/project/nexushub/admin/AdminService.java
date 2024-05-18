package com.project.nexushub.admin;

import com.project.nexushub.user.UpdateUserRequest;
import com.project.nexushub.user.User;
import com.project.nexushub.user.UserRepository;
import com.project.nexushub.role.Role;
import com.project.nexushub.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminResponse> getAllUsers() {
        // Retrieve all users
        List<User> users = userRepository.findAll();

        // Convert users to UserResponse DTOs
        return users.stream()
                .map(this::mapToAdminResponse)
                .collect(Collectors.toList());
    }

    public ResponseEntity<AdminResponse> getUserDetailByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(mapToAdminResponse(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<UpdateUserRequest> updatingUser(UUID userId, UpdateUserRequest updateUserRequest) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(userId);

            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setUsername(updateUserRequest.getUsername());
                String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
                existingUser.setPassword(encodedPassword);
                existingUser.setEmail(updateUserRequest.getEmail());
                existingUser.setPhone_number(updateUserRequest.getPhone_number());

                userRepository.save(existingUser);

                return ResponseEntity.ok(updateUserRequest);
            } else {
                // If the product with the given productId doesn't exist, return not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the update process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void deletingUser(UUID userId) {

        Optional<User> userOptional = userRepository.findUserById(userId);

        if (userOptional.isPresent()) {
            try {
                userRepository.deleteById(userId);
                ResponseEntity.ok("User deleted successfully");
            } catch (Exception e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
            }
        } else {
            ResponseEntity.notFound().build();
        }
    }

    private AdminResponse mapToAdminResponse(User user) {
        Set<RoleType> roleTypes = user.getRoles().stream()
                .map(Role::getRoleType)
                .collect(Collectors.toSet());

        return AdminResponse.builder()
                .user_id(user.getUser_id())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone_number(user.getPhone_number())
                .created(user.getCreated())
                .roles(roleTypes)
                .build();
    }

}
