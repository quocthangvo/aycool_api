package com.example.shopapp_api.filter;

import com.example.shopapp_api.components.JwtTokenUtil;
import com.example.shopapp_api.entities.users.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    //kt cac request di qua, tru register, login
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            assert authHeader != null;
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);//request k yeu cau token

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        if ("GET".equalsIgnoreCase(request.getMethod())
//                ||
//                "POST".equalsIgnoreCase(request.getMethod()) ||
//                "DELETE".equalsIgnoreCase(request.getMethod()) ||
//                "PUT".equalsIgnoreCase(request.getMethod())
        ) {
            return true;
        }
        //request k can token
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/users", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/sub_categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/materials", apiPrefix), "GET"),
                Pair.of(String.format("%s/colors", apiPrefix), "GET"),
                Pair.of(String.format("%s/sizes", apiPrefix), "GET"),
                Pair.of(String.format("%s/product_details", apiPrefix), "GET"),
                Pair.of(String.format("%s/prices", apiPrefix), "GET"),
                Pair.of(String.format("%s/auths/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auths/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/orders", apiPrefix), "GET"),
                Pair.of(String.format("%s/warehouse", apiPrefix), "GET"),
                Pair.of(String.format("%s/purchases", apiPrefix), "GET")
//                Pair.of(String.format("%s/cart", apiPrefix), "GET")

        );
        for (Pair<String, String> byPassToken : byPassTokens) {
            if (request.getServletPath().contains(byPassToken.getFirst()) &&
                    request.getMethod().equals(byPassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
