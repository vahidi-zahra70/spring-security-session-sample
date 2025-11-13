package com.pracetice.spring_security_session.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableRedisHttpSession
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user_1 = User.withUsername("zahra")
                .password(passwordEncoder().encode("12345")) 
                .roles("USER")
                .build();

        UserDetails user_2 = User.withUsername("ali")
                .password(passwordEncoder().encode("12345")) 
                .roles("USER")
                .build();

        UserDetails user_3 = User.withUsername("ahmad")
                .password(passwordEncoder().encode("12345")) 
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user_1,user_2,user_3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http, UserDetailsService uds) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(uds)
//                .and()
//                .build();
//    }
}
