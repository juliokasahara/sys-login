package com.syslogin.security;

import com.syslogin.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private static final Long EXPIRE_DURATION = (long) (24 * 60 * 60 * 1000); // 24 hr
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${security.jwt.signing.key}")
    private String secretKey;
    @Value("${security.jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    public String generateAccessToken(User user,Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim("id", user.getId())
                .setSubject(user.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuer("issuer")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or has only whitepace");
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation true", ex);
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}