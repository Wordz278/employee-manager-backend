package com.kwambi.employeesystem.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kwambi.employeesystem.constant.SecurityConstant;
import com.kwambi.employeesystem.domain.UserPrincipal;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenProvider {
    
    @Value("${jwt.secret}")
    private String secret;

    //Generate Token After Login
    public String generateJwtToken(UserPrincipal userPrincipal){
        String[] claims = getClaimsFromUser(userPrincipal);

        return JWT.create()
            .withIssuer(SecurityConstant.KWAMBI_LLC)
            .withAudience(SecurityConstant.KWAMBI_ADMINISTRATION)
            .withIssuedAt(new Date())
            .withSubject(userPrincipal.getUsername())
            .withArrayClaim(SecurityConstant.AUTHORITIES, claims)
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    //Get Authrorities from the token
    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //Get Authentication
    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    //Check if token is valid
    public boolean isTokenValid(String username, String token){
        JWTVerifier verifier = getJWTVerifier();
        return org.apache.commons.lang3.StringUtils.isNoneEmpty(username) && !isTokenExpired(verifier, token);
    }

    //get the Subject
    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    //Check if the token expired
    private boolean isTokenExpired(JWTVerifier verifier, String token){
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    //Get claims from token
    private String[] getClaimsFromToken(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
    }


    //Token Verifier
    private JWTVerifier getJWTVerifier(){
        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstant.KWAMBI_LLC).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(SecurityConstant.TOEKN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    //Get the user claims
    private String[] getClaimsFromUser(UserPrincipal user){
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
