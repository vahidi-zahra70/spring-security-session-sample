package com.pracetice.spring_security_session.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pracetice.spring_security_session.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;


@Component("customAccessDeniedHandler")
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws  IOException {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(accessDeniedException.getMessage())
                .localizedMessage("ACCESS_IS_DENIED")
                .timestamp(Instant.now())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        OutputStream responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorDTO);
        responseStream.flush();
    }
}
