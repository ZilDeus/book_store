package com.zildeus.book_store.config.jwt;

import com.zildeus.book_store.config.user.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAccessTokenFilter extends OncePerRequestFilter {

    private final JwtDecoder decoder;
    private final JWTUtils utils;
    private final UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(SecurityContextHolder.getContext().getAuthentication()!=null) {
                filterChain.doFilter(request,response);
                return;
            }
            Jwt token = GetTokenFromHeader(request);
            if(token==null)
            {
                filterChain.doFilter(request,response);
                return;
            }
            if (!utils.IsTokenExpired(token)&&detailsService.loadUserByUsername(token.getSubject())!=null) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UserDetails userDetails = detailsService.loadUserByUsername(token.getSubject());
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
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        }
    }
    private Jwt GetTokenFromHeader(HttpServletRequest request){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!authHeader.startsWith("Bearer "))
            return null;
        final String token = authHeader.substring(7);
        return decoder.decode(token);
    }
}
