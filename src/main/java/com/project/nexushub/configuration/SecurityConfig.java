package com.project.nexushub.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/nexusHub/**").permitAll()
//                        .requestMatchers("/nexusHub/admin").hasRole("ADMIN")

//                        .requestMatchers("/nexushub/admin/**").hasRole(RoleType.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET,"/nexushub/admin").hasAuthority(ADMIN_READ.name())
//                        .requestMatchers(HttpMethod.GET,"/nexushub/admin/find").hasAuthority(ADMIN_READ.name())
//                        .requestMatchers(HttpMethod.DELETE,"/nexushub/admin/delete").hasAuthority(ADMIN_DELETE.name())
//
//                        .requestMatchers("/nexushub/user/**").hasAnyRole(ADMIN.name(), USER.name())
//                        .requestMatchers(HttpMethod.GET,"/nexushub/user/find").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
//                        .requestMatchers(HttpMethod.PUT,"/user/reset-password").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
//                        .requestMatchers(HttpMethod.DELETE,"/nexushub/user/delete").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
//
//                        .requestMatchers("/product").permitAll()
//                        .requestMatchers("/product/**").permitAll()
//
//                        .requestMatchers("/category").permitAll()
//                        .requestMatchers("/category/**").permitAll()
//
//                        .requestMatchers("/user/cart").hasAnyRole(ADMIN.name(), USER.name())
//                        .requestMatchers(HttpMethod.GET,"/nexushub/cart").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
//                        .requestMatchers(HttpMethod.GET,"/nexushub/cart/user").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
//                        .requestMatchers(HttpMethod.POST,"/nexushub/cart/add").hasAnyAuthority(ADMIN_POST.name(), USER_POST.name())
                        .anyRequest().authenticated());

        return http.build();
    }

}
