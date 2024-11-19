package com.example.shopapp_api.components;

import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.exceptions.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration; //lưu vào biến môi trường

    @Value("${jwt.secretKey}")
    private String secretKey;

    //    chuyển hóa user trong entities thành java string security
    public String generateToken(User user) throws InvalidParamException {
        Map<String, Object> claims = new HashMap<>();
//        this.generateSecretKey();
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) //how to extract claims from this
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) //1000 là đổi 1s ra 1000millis
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParamException("Không tạo jwt token, lỗi: " + e.getMessage());

        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    //chạy 1 lần để lấy key
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; //256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extracAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //extract riêng claim mong muốn
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //kt username, token còn thoi han k, để biet token do hop le
    public boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}

