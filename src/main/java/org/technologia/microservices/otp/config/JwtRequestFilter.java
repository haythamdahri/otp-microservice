package org.technologia.microservices.otp.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.technologia.microservices.otp.utils.JwtTokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Haytham DAHRI
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException ignored) {
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(username);
            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtils.validateToken(jwtToken, userDetails)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                /* After setting the Authentication in the context, we specify that the current
                user is authenticated. So it passes the Spring Security Configurations successfully. */
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}