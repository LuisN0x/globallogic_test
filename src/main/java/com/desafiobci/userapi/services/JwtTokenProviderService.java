package com.desafiobci.userapi.services;

import org.springframework.stereotype.Service;

@Service
public interface JwtTokenProviderService {

    public String generateToken(String email);

    public boolean validateToken(String token, String userEmail);

    public String decodeTokenToEmail(String token);
}
