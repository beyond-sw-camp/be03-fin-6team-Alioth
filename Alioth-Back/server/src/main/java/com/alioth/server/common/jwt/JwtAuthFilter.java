package com.alioth.server.common.jwt;

import com.alioth.server.common.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;


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
            String accessToken = parseBearerToken(request);
            String name = jwtTokenProvider.decodeJwtPayloadSubject(accessToken).split(":")[0];
            String reqRefreshToken = parseBearerTokenRefresh(request);
            String refreshToken = redisService.getValues(name + ":RefreshToken");

            if(reqRefreshToken == refreshToken) {
                String newAccessToken = jwtTokenProvider.recreateAccessToken(accessToken);
                User user = parseUserSpecification(newAccessToken);
                AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.getAuthorities());
                authenticated.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
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
