package com.example.videogamereviewservice.security;

import com.example.videogamereviewservice.security.jwt.JwtFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints
                        .requestMatchers("/auth/**").permitAll()

                        // Static resources (CSS, JS, Images)
                        .requestMatchers("/*.html", "/Login_and_Registration.html",
                                "/Main_page.html", "/Profile.html", "/Game_page_*.html")
                        .permitAll()
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**",
                                "/Avatars/**", "/Images/**", "/assets/**, ", "/favicon.ico")
                        .permitAll()
                        .requestMatchers("/me/**").authenticated()

                        // Swagger UI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/api-docs/**",
                                "/webjars/**", "/swagger-resources/**")
                        .permitAll()

                        // Public APIs (GET только)
                        .requestMatchers(HttpMethod.GET, "/genres/**", "/platforms/**",
                                "/tags/**", "/games/**", "/reviews/**", "/users/**")
                        .permitAll()

                        // Всё остальное требует авторизации
                        .anyRequest().authenticated()
                )
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    
}
