package com.example.demo.security;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RoleHeaderFilter extends OncePerRequestFilter {

    static final String HEADER = "X-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String role = request.getHeader(HEADER);
        if (role != null && !role.isBlank()) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "header-user",
                    null,
                    List.of(new SimpleGrantedAuthority(toRoleAuthority(role))));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private static String toRoleAuthority(String role) {
        String value = role.trim();
        if (value.regionMatches(true, 0, "ROLE_", 0, 5)) {
            return "ROLE_" + value.substring(5).toUpperCase(Locale.ROOT);
        }
        return "ROLE_" + value.toUpperCase(Locale.ROOT);
    }
}
