package com.desafiobci.userapi.services;

import com.desafiobci.userapi.exceptions.LoginValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProviderServiceImpl implements JwtTokenProviderService {

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private final Key jwtSecretKey;

    public JwtTokenProviderServiceImpl(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    @Override
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token, String userEmail) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject().equals(userEmail) &&
                    isTokenExpired(claims.getExpiration());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public String decodeTokenToEmail(String token) {
        try {
            //Eliminar el prefijo "Bearer "
            token = token.substring(7);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
