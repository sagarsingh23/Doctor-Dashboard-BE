package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import io.jsonwebtoken.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
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
        claims.put("details",tokenClaims);
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
        System.out.println("username:"+claims);
        return claims.getSubject();
    }
    public Long getIdFromToken(HttpServletRequest request){
        String jwtToken;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwtToken= bearerToken.substring(7, bearerToken.length());
        }else
            jwtToken=null;
        System.out.println(jwtToken);
        if(validateToken(jwtToken)) {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            ModelMapper mapper = new ModelMapper();
            Claims details = mapper.map(claims.get("DoctorDetails"), Claims.class);
            System.out.println(details.getId());
            return details.getId();
        }
        return null;
    }
    // validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

}
