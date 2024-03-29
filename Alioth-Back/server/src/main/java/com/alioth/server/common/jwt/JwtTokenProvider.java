package com.alioth.server.common.jwt;

import com.alioth.server.common.redis.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@PropertySource("classpath:jwt.yml")
@Component
public class JwtTokenProvider {

    private final String accessSecretKey;
    private final String refreshSecretKey;
    private final long expirationMinutes;
    private final long refreshExpirationMinutes;
    private final RedisService redisService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtTokenProvider(@Value("${access-key}") String accessSecretKey,
                            @Value("${refresh-key}")String refreshSecretKey,
                            @Value("${access-expired}")long expirationMinutes,
                            @Value("${refresh-expired}")long refreshExpirationMinutes,
                            RedisService redisService) {
        this.accessSecretKey = accessSecretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.expirationMinutes = expirationMinutes;
        this.refreshExpirationMinutes = refreshExpirationMinutes;
        this.redisService = redisService;
    }

    public String createAccessToken(String userSpecification) {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, accessSecretKey.getBytes())
                .setSubject(userSpecification)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
                .compact();
    }

    public String createRefreshToken() {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey.getBytes())
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationMinutes, ChronoUnit.MINUTES)))
                .compact();
    }


    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String subject = decodeJwtPayloadSubject(oldAccessToken);

//        userRefreshTokenRepository.findByUserIdAndReissueCountLessThan(
//                        Long.parseLong(subject.split(":")[0]), reissueLimit)
//                .ifPresentOrElse(
//                        UserRefreshToken::increaseReissueCount,
//                        () -> {
//                            throw new ExpiredJwtException(null, null, "Refresh token expried.");
//                        }
//                );
        return createAccessToken(subject);
    }


//    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
//        validateAndParseToken(refreshToken);
//        String memberCode = decodeJwtPayloadSubject(oldAccessToken).split(":")[0];
//        //String redis_refreshToken = redisService.getValues(memberCode + ":RefreshToken");
//
//        userRefreshTokenRepository.findByUserIdAndReissueCountLessThan(Long.parseLong(memberId), reissueLimit)
//                .filter(memberRefreshToken -> memberRefreshToken.validateRefreshToken(refreshToken))
//                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
//
//    }
    public Claims validateRefreshToken(String refreshToken) {
        Key key = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }


    public String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]),
                        StandardCharsets.UTF_8),
                        Map.class)
                .get("sub").toString();
    }



    private Jws<Claims> validateAndParseToken(String refreshToken) {
        // validateTokenAndGetSubject에서 따로 분리
        return Jwts.parser()
                .setSigningKey(accessSecretKey.getBytes())
                .parseClaimsJws(refreshToken);
    }

    public String validateTokenAndGetSubject(String token) {
        return validateAndParseToken(token).getBody().getSubject();
    }



}
