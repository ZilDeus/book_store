package com.zildeus.book_store.service;

import com.zildeus.book_store.config.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenGenerationService {
    private final JwtEncoder encoder;
    public String GenerateAccessToken(Authentication authentication) {
        String permissions = GetUserPermissionFromRoles(GetUserRoles(authentication));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("zildeus")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", permissions)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    public String GenerateRefreshToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("zildeus")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15 , ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", "REFRESH_TOKEN")
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    private String GetUserRoles(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
    private String GetUserPermissionFromRoles(String roles){
        List<String> perms = new ArrayList<>();
        Arrays.stream(roles.split(" ")).forEach(role->{
            perms.addAll(UserRole.getRolePermissions(UserRole.valueOf(role)));
        });
        return String.join(" ", perms);
    }
}
