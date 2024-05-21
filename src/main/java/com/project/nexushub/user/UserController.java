package com.project.nexushub.user;

import com.project.nexushub.authentication.AuthenticationRequest;
import com.project.nexushub.admin.DeleteUserRequest;
import com.project.nexushub.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "https://dev--nexushub-mall.netlify.app")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/user/find")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        UserResponse userResponse = userService.getUserByEmail(email);

        if (userResponse != null) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getRoleType() == RoleType.ADMIN);

                if (isAdmin) {
                    return ResponseEntity.badRequest().build();
                }

                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/totalUsers")
    public ResponseEntity<Long> getTotalUsers() {
        Long totalUsers = userRepository.count();
        return ResponseEntity.ok(totalUsers);
    }

    @PutMapping("/user/reset-password")
//    @PreAuthorize("hasAnyAuthority('admin:update', 'user:update')")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
       String newPassword = userService.resetUserPasswordByEmail(email);
        if (newPassword != null) {
            return ResponseEntity.ok(newPassword);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/delete")
//    @PreAuthorize("hasAnyAuthority('admin:delete', 'user:delete')")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        boolean deleted = userService.deleteUserByEmailAndPassword(deleteUserRequest.getEmail(), deleteUserRequest.getPassword());
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete user");
        }
    }

    @PutMapping("/user/change-password")
    public String updatePassword(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        Optional<User> userOptional = userRepository.findUserByUsername(authenticationRequest.getUsername());

        if (userOptional.isEmpty()) {
            throw new Exception("User does not exist");
        }

        User user = userOptional.get();

        // Check if the new password is provided and not the same as the old one
        String newPassword = authenticationRequest.getPassword();
        if (newPassword == null || newPassword.equals(user.getPassword())) {
            throw new Exception("New password is required and should be different from the old password");
        }

        // Update the user's password with proper hashing (assuming passwordEncoder is a configured bean)
        user.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));

        userRepository.save(user);

        return "Password changed successfully";
    }
}
