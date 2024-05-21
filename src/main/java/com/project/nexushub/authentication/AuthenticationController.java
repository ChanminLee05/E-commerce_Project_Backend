package com.project.nexushub.authentication;

import com.project.nexushub.user.User;
import com.project.nexushub.user.UserRepository;
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
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        } else if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User Name is already registered");
        } else if (userRepository.existsByPhone_number(registerRequest.getPhone_number())) {
            return ResponseEntity.badRequest().body("Phone number is already registered");
        }
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
                return ResponseEntity.ok(authenticationResponse);
            }
        }

        // If the username or password is incorrect, return unauthorized response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthenticationResponse> adminLogin(@RequestBody AuthenticationRequest authenticationRequest) {
        String adminName = authenticationRequest.getUsername();
        String adminPassword = authenticationRequest.getPassword();

        Optional<User> userOptional = userRepository.findByUsername(adminName);
        if (userOptional.isPresent() &&
                userOptional.get().getEmail().equals("admin@nexushub.com") &&
                userOptional.get().getUsername().equals("Admin")) {
            User user = userOptional.get();
            if (passwordEncoder.matches(adminPassword, user.getPassword())) {
                AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
                return ResponseEntity.ok(authenticationResponse);
            }
        }

        // If the username or password is incorrect, return unauthorized response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
