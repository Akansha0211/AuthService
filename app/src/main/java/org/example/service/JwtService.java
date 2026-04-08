package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
* stores token, chceking token expied or not
* jwt token signed with a secret key
*/
@Service
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

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /*
    * this method checks whether token is valid or not
    * based on userdetails coming from Database , gets username matches it from username in token
    *  and checks if token not expired*/
    public Boolean validateToken (String token, UserDetails userDetails){
        final String username = extractUsername(token); // extrcating user name from token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    /*
    * Big picture
    * creating token(THIS METHOD BELOW) --> extracting claims(Creating JWT using parser(i.e builder)
    * Claims having several method like extracting username, extracting expiration : Claims::getExpiration ( USING IT AS ExtractClaim method --> where we are JWT parser, using token creatig JWT  ans then applying method on it i.e apply()
    * --> returns apply(claims) which can be with return type String or Date
    * */
    private String createToken(Map<String,Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
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
