package com.project.nexushub.configuration;

import com.project.nexushub.authentication.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extract jwtToken from auth header
        jwt = authHeader.substring(7);
        //extract userEmail
        userEmail = jwtService.extractUsername(jwt);
        try {
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Log success
                    logger.info("Authentication success for user: {}");
                } else {
                    // Log token validation failure
                    logger.warn("Token validation failed for user: {}");
                }
            } else {
                // Log userEmail is null or Authentication already present
                logger.debug("User email is null or Authentication already present");
            }
        } catch (ExpiredJwtException e) {
            // Log any exception during authentication
            logger.warn("Token expired for user: {}");
            throw new BadCredentialsException("Token expired", e);
        } catch (UsernameNotFoundException e) {
            // Log user details not found during authentication
            logger.warn("User details not found for user: {}");
            throw e;
        } catch (BadCredentialsException e) {
            // Log token validation failure
            logger.warn("Token validation failed for user: {}");
            throw e;
        } catch (Exception e) {
            // Log any unexpected exception during authentication
            logger.error("Unexpected exception during authentication: {}");
            throw new AuthenticationServiceException("Unexpected exception during authentication", e);
        }
        filterChain.doFilter(request, response);
    }
}
