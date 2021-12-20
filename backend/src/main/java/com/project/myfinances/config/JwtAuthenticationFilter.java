package com.project.myfinances.config;

import com.project.myfinances.service.JwtService;
import com.project.myfinances.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_ATTRIBUTE = "Authorization";
    private static final String PREFIX_ATTRIBUTE = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HEADER_ATTRIBUTE);

        if (authorization != null && authorization.startsWith(PREFIX_ATTRIBUTE))  {
            String token = authorization.split(" ")[1];
            boolean isValidToken = jwtService.isValidToken(token);

            if (isValidToken) {
                String login = jwtService.getUserLogin(token);

                UserDetails authenticatedUser = userDetailsService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(authenticatedUser,
                                                                    null, authenticatedUser.getAuthorities());

                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
