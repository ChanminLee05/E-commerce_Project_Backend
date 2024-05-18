package com.project.nexushub.authentication;

import com.project.nexushub.role.Role;
import com.project.nexushub.role.RoleRepository;
import com.project.nexushub.shoppingCart.Cart;
import com.project.nexushub.shoppingCart.ShoppingCartRepository;
import com.project.nexushub.user.User;
import com.project.nexushub.user.UserRepository;
import com.project.nexushub.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ShoppingCartRepository shoppingCartRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone_number(registerRequest.getPhone_number())
                .created(LocalDate.now())
                .build();

        Role userRole = roleRepository.findByRoleName(RoleType.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        user.addRole(userRole);

        if (registerRequest.getEmail().equals("admin@nexushub.com")) {
            Role adminRole = roleRepository.findByRoleName(RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            user.addRole(adminRole);
        }

        userRepository.save(user);

        Cart shoppingCart = new Cart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUser_id())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect password. Authentication failed.", e);
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User with username " + username + " not found.", e);
        } catch (LockedException e) {
            throw new RuntimeException("User account is locked. Please contact support.", e);
        } catch (DisabledException e) {
            throw new RuntimeException("User account is disabled.", e);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed.", e);
        }
        var user = userRepository.findUserByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUser_id())
                .build();
    }
}
