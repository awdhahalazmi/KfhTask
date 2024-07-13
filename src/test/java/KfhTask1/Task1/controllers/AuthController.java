package KfhTask1.Task1.controllers;

import KfhTask1.Task1.bo.auth.AuthenticationResponse;
import KfhTask1.Task1.bo.auth.LoginRequest;
import KfhTask1.Task1.bo.auth.SignupRequest;
import KfhTask1.Task1.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
