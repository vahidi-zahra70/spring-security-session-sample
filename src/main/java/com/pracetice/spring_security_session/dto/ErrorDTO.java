package com.pracetice.spring_security_session.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private HttpStatus status;
    private Instant timestamp;
    private String localizedMessage;
    private String code;
    private String message;

}