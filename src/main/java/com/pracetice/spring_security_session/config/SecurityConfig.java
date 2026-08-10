package com.pracetice.spring_security_session.config;

import com.pracetice.spring_security_session.filter.MyAuthenticationFilter;
import com.pracetice.spring_security_session.handler.CustomAccessDeniedHandler;
import com.pracetice.spring_security_session.handler.CustomAuthenticationEntryPoint;
import com.pracetice.spring_security_session.provider.OtpAuthenticationProvider;
import com.pracetice.spring_security_session.provider.UsernamePasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        AuthenticationManager authManager = new ProviderManager(usernamePasswordAuthenticationProvider,otpAuthenticationProvider);
//        return http
//                .formLogin(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login").permitAll()
//                        .anyRequest().authenticated())
//                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(new MyAuthenticationFilter(authManager), UsernamePasswordAuthenticationFilter.class)
//                .build();
//
//    }

    /**
     * NullRequestCache This prevents anonymous 401 requests from creating unnecessary sessions and Redis keys.
     * ChangeSessionIdAuthenticationStrategy is responsible to change the sessionId after each successful login
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SessionRegistry sessionRegistry,
                                                   SessionAuthenticationStrategy sessionAuthenticationStrategy) throws Exception {
        AuthenticationManager authManager = new ProviderManager(usernamePasswordAuthenticationProvider, otpAuthenticationProvider);
        return http
                .requestCache((cache) -> cache
                        .requestCache( new NullRequestCache())
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(
                        new MyAuthenticationFilter(authManager, sessionAuthenticationStrategy),
                        UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public SessionRegistry sessionRegistry(RedisIndexedSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionStrategy =
                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        concurrentSessionStrategy.setMaximumSessions(1);
        concurrentSessionStrategy.setExceptionIfMaximumExceeded(false);

        CompositeSessionAuthenticationStrategy delegate =
                new CompositeSessionAuthenticationStrategy(List.of(
                        concurrentSessionStrategy,
                        new ChangeSessionIdAuthenticationStrategy(),
                        new RegisterSessionAuthenticationStrategy(sessionRegistry)
                ));

        // The username/password step only requests an OTP. Register a session
        // after the OTP provider returns a fully authenticated token.
        return (authentication, request, response) -> {
            if (authentication.isAuthenticated()) {
                delegate.onAuthentication(authentication, request, response);
            }
        };
    }
}
