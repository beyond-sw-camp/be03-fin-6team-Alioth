package com.alioth.server.common.config;


import com.alioth.server.common.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/server/").permitAll()
                                .requestMatchers(LoginApiUrl).permitAll()
                                .requestMatchers(SwaggerUrl).permitAll()
                                .requestMatchers(DummyApiUrl).permitAll()
                                .requestMatchers("/server/api/v1/fcm/send").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class);


        return httpSecurity.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> httpMethodList = List.of(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
        );
        List<String> ipList = List.of("http://localhost:9000" , "https://www.alioth.site", "http://www.alioth.site");

        config.setAllowCredentials(true);
        config.setAllowedMethods(httpMethodList);
        config.setAllowedOrigins(ipList);
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] LoginApiUrl = {
            "/server/api/login",
            "/server/api/*/logout",
            "/server/api/members/create",
            "/server/api/send-verification",
            "/server/api/verify-code",
            "/server/api/members/*/password",
            "/server/api/members/validate/*",
            "/server/api/image/*"
    };

    private static final String[] DummyApiUrl = {
            "/server/dummy/**"
    };

    private static final String[] SwaggerUrl = {
//            "/api/**",
            "/server/swagger-ui/**",
            "/server/swagger-ui.html",
            "/server/v3/api-docs/**",
            "/server/v3/api-docs.yaml"
    };
}
