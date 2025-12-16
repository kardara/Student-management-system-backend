package com.kardara.studentManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kardara.studentManagement.service.UsersDetailsOauth2Services;
import com.kardara.studentManagement.service.UsersDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${front.end.ip}")
    private String frontEndIp;

    @Autowired
    private UsersDetailsService usersDetailsService;

    @Autowired
    private UsersDetailsOauth2Services usersDetailsOauth2Services;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(customizer -> customizer.disable())
                // .sessionManagement(c ->
                // c.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // shall try
                // .NEVER
                .cors(Customizer.withDefaults()) // ✅ enable CORS handling ??? why is it working here

                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(au -> au
                        .requestMatchers("/auth/login", "/auth/register", "/auth/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll() // for Oauth2
                        .requestMatchers("/application/apply","/academicunit/get" ).permitAll() /// for applications
                        // .anyRequest().authenticated()
                        .anyRequest().permitAll()
                        )
                .formLogin(form -> form.disable())
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(usersDetailsOauth2Services))
                        .defaultSuccessUrl(frontEndIp + "/auth/oauth2/success", true))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(8));
        provider.setUserDetailsService(usersDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
