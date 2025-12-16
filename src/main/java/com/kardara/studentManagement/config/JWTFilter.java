package com.kardara.studentManagement.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kardara.studentManagement.service.JWTUtilities;
import com.kardara.studentManagement.service.UsersDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtilities jwtUtilities;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String authenticationHeader = req.getHeader("Authorization");
        String token = null;
        String username = null;

        try {

            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")
                    && authenticationHeader.length() > 8) {
                token = authenticationHeader.substring(7);
                username = jwtUtilities.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = applicationContext.getBean(UsersDetailsService.class)
                        .loadUserByUsername(username);

                if (jwtUtilities.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }

            filterChain.doFilter(req, res);
            return;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Exepired Token");
            writeErrorResponse(res, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired."); // 401
            return;
        } catch (io.jsonwebtoken.JwtException e) {
            System.out.println("invalid Token");
            writeErrorResponse(res, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
            return;
        } catch (Exception e) {
            System.out.println("JWT Filter exception");
            writeErrorResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred."); // 500
            return;
        }
    }

    private void writeErrorResponse(HttpServletResponse res, int statusCode, String message) throws IOException {
        System.out.println("Committed response: " + res.isCommitted());

        /// TO solve, need to implement is properly (Use the CORS class)
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        res.setHeader("Access-Control-Max-Age", "3600");

        res.setStatus(statusCode);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("{\"message\": \"" + message + "\"}");
        res.getWriter().flush();

    }
}
