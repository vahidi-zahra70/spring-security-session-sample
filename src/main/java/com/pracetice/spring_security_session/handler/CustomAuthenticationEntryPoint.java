package com.pracetice.spring_security_session.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pracetice.spring_security_session.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;

@Component("customAuthenticationEntryPoint")
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(authException.getMessage())
                .localizedMessage(authException.getMessage())
                .timestamp(Instant.now()).build();
//                .subErrors(authException instanceof BaamiamManagerAuthenticationException ? ((BaamiamManagerAuthenticationException) authException).getSubErrors() : new ArrayList<>()).build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorDTO);
        responseStream.flush();
    }
}
