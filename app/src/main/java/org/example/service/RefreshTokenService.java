package org.example.service;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    /*
    * When JWT expires , we get refreshToken request, creates new JWT
    * so that we don't get login request
    * Refresh token is still valid
    *
    * When RefreshToken expired then creates RefreshToken
    * This method will be called when Login request is made(which happens when JWT and Refresh Token expires)
    * */
    public RefreshToken createRefreshToken(String username){
        UserInfo userInfoExtracted = userRepository.findUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) <0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh toke  is expired, Please make a new login");
        }
        return token;
    }
}
