package com.project.nexushub.user;

import com.project.nexushub.authentication.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserResponse getUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return UserResponse.builder()
                    .username(user.getUsername())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    public boolean deleteUserByEmailAndPassword(String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.deleteByEmail(email);
                return true;
            } else {
                throw new IllegalArgumentException("Incorrect password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public Optional<User> getUserById(UUID userId) {
        if (userId != null) {
            return userRepository.findUserById(userId);
        }
        return Optional.empty();
    }

    public String resetUserPasswordByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {

            String newPassword = generateNewPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            sendNewPasswordToEmail(email, newPassword);

            return newPassword;

        } else {
            throw new UsernameNotFoundException("User with the provided email does not exist");

        }
    }

    private void sendNewPasswordToEmail(String email, String newPassword) {
        String subject = "Password Reset Successful";
        String body = "Your Password has been successfully rest. Your new password is: " + newPassword;
        emailService.sendEmail(email, subject, body);
    }

    private String generateNewPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%&*?";

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
        while (password.length() < 8 || !hasNumber || !hasSpecialCharacter) {
            password.setLength(0);

            for (int i = 0; i < 12; i++) {
                password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
            }

            // Check if the password contains at least one number
            hasNumber = password.toString().matches(".*\\d.*");

            // Check if the password contains at least one special character
            hasSpecialCharacter = password.toString().matches(".*[" + Pattern.quote(specialCharacters) + "].*");
        }
        return password.toString();
    }

    public User getCurrentLoggedInUser(Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and the principal is of type User
        if (auth != null && auth.getPrincipal() instanceof User) {
            // Cast the principal to User and return it
            return (User) auth.getPrincipal();
        } else {
            return null;
        }

    }
}
