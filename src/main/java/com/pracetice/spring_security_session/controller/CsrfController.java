package com.pracetice.spring_security_session.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    /**
     * Resolving the deferred token causes CookieCsrfTokenRepository to write
     * the XSRF-TOKEN cookie. Clients copy its value to X-XSRF-TOKEN on unsafe
     * requests (POST, PUT, PATCH and DELETE).
     */
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }
}
