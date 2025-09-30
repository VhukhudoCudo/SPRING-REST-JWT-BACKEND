
package com.example.demo.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProtectedController {
    @GetMapping("/hello")
    public String hello(Authentication auth) {
        return "Hello, " + auth.getName();
    }
}
