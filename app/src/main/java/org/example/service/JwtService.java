package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/*
* stores token, chceking token expied or not
* jwt token signed with a secret key
*/
public class JwtService {
    public static final String SCRET_KEY = "397b70de93d501d2b6be56fcc8ec1ccac86d770d8fa85ef76cc4c8c0ac44331a";  // 256 bits randomly generated key
    // key to be stored inside key manager or env file

    // jab bhi koi token aata hain http request main usme claims saath m ayenge
    // claims : DS having everuthing : username, expired
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /*
    * This method extracts all claims : ceates JWT parser, uses token amd signing key
    * if the token ws created using signing key so it can extract all cliams
    * T --> any datatype --> String, Long, or  any object
    * Here T is String from extractClaim method
    * Functional Interface : Function<T,R> here T is Claims , and R is String
    * */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolveer){
        final Claims claims = extractAllClaims(token);
        return claimResolveer.apply(claims);
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims:: getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SCRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
     }
}
