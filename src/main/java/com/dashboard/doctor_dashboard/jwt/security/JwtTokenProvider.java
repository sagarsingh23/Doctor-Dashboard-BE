package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // generate token
    public String generateToken(Authentication authentication, Claims tokenClaims) {
        String email = authentication.getName();

        var currentDate = new Date();
        var expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);
        Map<String,Object> claims= new HashMap<>();
        claims.put("DoctorDetails",tokenClaims);
        claims.put("role",tokenClaims.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    // get username from the token
    public String getUsernameFromJWT(String token) {
        var claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public Long getIdFromToken(HttpServletRequest request){
        String jwtToken;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwtToken= bearerToken.substring(7, bearerToken.length());
            validateToken(jwtToken);
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            var mapper = new ModelMapper();
            Claims details = mapper.map(claims.get("DoctorDetails"), Claims.class);
            return details.getDoctorId();
        }else{
        throw new APIException("JWT claims string is empty.");
        }
    }
    // validate JWT token
    public boolean validateToken(String token) {
        try {
            log.info(token);
            log.info(jwtSecret);
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new APIException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new APIException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new APIException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new APIException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new APIException("JWT claims string is empty.");
        }
    }

}
