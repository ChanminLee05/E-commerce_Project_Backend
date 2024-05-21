package com.project.nexushub.admin;

import com.project.nexushub.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "https://dev--nexushub-mall.netlify.app")
public class AdminController {

    private final AdminService adminService;

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
