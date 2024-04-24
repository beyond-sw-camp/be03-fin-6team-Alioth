package com.alioth.server.common.jwt;

import com.alioth.server.common.redis.RedisService;
import com.alioth.server.common.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            if(token != null) {
                User user = parseUserSpecification(token);

                if (user != null) {
                    AbstractAuthenticationToken authenticated
                            = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
                    authenticated.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticated);
                }
            }
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException 메시지" + e.getMessage());
            String accessToken = parseBearerToken(request);
            String newAccessToken = jwtTokenProvider.recreateAccessToken(accessToken);
            String newRefreshToken = jwtTokenProvider.createRefreshToken();

            response.setHeader("access-Token", newAccessToken);
            response.setHeader("fresh-Token", newRefreshToken);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(
                    response.getWriter(),
                    CommonResponse.builder()
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .message("토큰을 재발행 합니다.")
                            .build()
            );

            //}
        } catch (Exception e) {
            request.setAttribute("exception" , e);
        }


        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    private String parseBearerTokenRefresh(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Refresh-Token"))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 1)
                .map(jwtTokenProvider::validateTokenAndGetSubject)
                .orElse(null)
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }



}
