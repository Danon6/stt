package tn.capgemini.stackquestion.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.capgemini.stackquestion.Utils.JwtUtil;
import tn.capgemini.stackquestion.services.jwt.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("🔥 [JwtFilter] Called for URI: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
            System.out.println("🔐 Authorization Header: " + token);

            // Vérifie structure du JWT (3 parties)
            if (token.split("\\.").length == 3) {
                try {
                    username = jwtUtil.extractUsername(token);
                    System.out.println("✅ Token extracted username: " + username);
                } catch (Exception e) {
                    System.out.println("❌ JWT parsing failed: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Malformed JWT token: " + token);
            }
        } else {
            System.out.println("❗ No Authorization header or invalid format");
        }

        // Si tout est bon, on authentifie l'utilisateur
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                System.out.println("✅ Token is valid. Authenticating user...");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("❌ Token validation failed");
            }
        }

        filterChain.doFilter(request, response);
    }
}
