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
import com.zildeus.book_store.config.user.ApplicationUserDetailsService;
import com.zildeus.book_store.repository.JWTRefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final JWTRefreshTokenRepository refreshTokenRepository;
    private final RsaKeyPair rsaKeyPair;


    @Order(1)
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception{
        return http
                .securityMatcher(new AntPathRequestMatcher("/api/user/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth.anyRequest().authenticated())
                .oauth2ResourceServer(auth2->auth2.jwt(withDefaults()))
                .addFilterBefore(new JWTAccessTokenFilter(jwtUtils()), UsernamePasswordAuthenticationFilter.class)
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
    SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception{
        return http
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth.anyRequest().permitAll())
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    SecurityFilterChain signInFilterChain(HttpSecurity http) throws Exception{
        return http.securityMatcher(new AntPathRequestMatcher("/auth/sign-in/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .userDetailsService(applicationUserDetailsService)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e
                        ->  e.authenticationEntryPoint((request, response, authException)
                        ->  response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage()))
                )
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    SecurityFilterChain RefreshTokenFilterChain(HttpSecurity http) throws Exception{
        return http.securityMatcher(new AntPathRequestMatcher("/auth/token/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .userDetailsService(applicationUserDetailsService)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTRefreshTokenFilter(jwtUtils()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e
                        ->  e.authenticationEntryPoint((request, response, authException)
                        ->  response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage()))
                )
                .httpBasic(withDefaults())
                .build();
    }
    @Order(4)
    @Bean
    SecurityFilterChain signUpFilterChain(HttpSecurity http) throws  Exception{
        return http.securityMatcher(new AntPathRequestMatcher("/auth/sign-up/**"))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth->auth.anyRequest().permitAll()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
    JWTUtils jwtUtils(){
        return new JWTUtils(refreshTokenRepository,applicationUserDetailsService,jwtDecoder());
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
