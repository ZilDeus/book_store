package com.zildeus.book_store.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.zildeus.book_store.config.jwt.JWTAccessTokenFilter;
import com.zildeus.book_store.config.jwt.JWTRefreshTokenFilter;
import com.zildeus.book_store.config.jwt.JWTUtils;
import com.zildeus.book_store.config.user.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;
    private final RsaKeyPair rsaKeyPair;
    @Order(1)
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception{
        return http
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth.anyRequest().authenticated())
                .oauth2ResourceServer(auth2->auth2.jwt(withDefaults()))
                .addFilterBefore(new JWTAccessTokenFilter(jwtDecoder(),jwtUtils,userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(withDefaults())
                .build();
    }
    @Order(2)
    @Bean
    SecurityFilterChain signInFilterChain(HttpSecurity http) throws Exception{
        return http.securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e
                        ->  e.authenticationEntryPoint((request, response, authException)
                        ->  response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage()))
                )
                .httpBasic(withDefaults())
                .build();
    }
    @Order(3)
    @Bean
    SecurityFilterChain signUpFilterChain(HttpSecurity http) throws  Exception{
        return http.securityMatcher(new AntPathRequestMatcher("sign-up/**"))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth->auth.anyRequest().permitAll()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
    @Order(3)
    @Bean
    SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity http) throws Exception{
        return http.securityMatcher(new AntPathRequestMatcher("refresh-token"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .oauth2ResourceServer(auth2->auth2.jwt(withDefaults()))
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTRefreshTokenFilter(jwtDecoder(),jwtUtils,userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e
                        ->  e.authenticationEntryPoint((request, response, authException)
                        ->  response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage()))
                )
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyPair.publicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyPair.publicKey()).privateKey(rsaKeyPair.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

}
