package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.DoctorClaims;
import io.jsonwebtoken.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // generate token
    public String generateToken(Authentication authentication, DoctorClaims doctorClaims) {
        String email = authentication.getName();
        var currentDate = new Date();
        var expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("DoctorDetails", doctorClaims)
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
        }else
            jwtToken=null;
        System.out.println(jwtToken);
        if(validateToken(jwtToken)) {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            ModelMapper mapper = new ModelMapper();
            DoctorClaims doctorDetails = mapper.map(claims.get("DoctorDetails"), DoctorClaims.class);
            System.out.println(doctorDetails.getDoctorId());
            return doctorDetails.getDoctorId();
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
