package com.zildeus.book_store.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTRefreshTokenFilter extends OncePerRequestFilter {
    private final JWTUtils utils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

        if(SecurityContextHolder.getContext().getAuthentication()!=null)
        {
            filterChain.doFilter(request,response);
            return;
        }
        Jwt token = utils.GetTokenFromHeader(request);
        if(token==null)
        {
            filterChain.doFilter(request,response);
            return;
        }
        if(utils.IsTokenValid(token)&&utils.IsRefreshTokenInDatabase(token.getTokenValue())){
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UserDetails userDetails = utils.GetTokenUser(token);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request,response);
        }
        catch (UsernameNotFoundException | JwtValidationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
