package com.pracetice.spring_security_session.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String updateUser() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
