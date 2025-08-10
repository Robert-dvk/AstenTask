package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.user.*;
import avmb.desafio.AstenTask.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO auth) {
        var loginResponse = authenticationService.login(auth);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO register) {
        String response = authenticationService.register(register);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String response = authenticationService.logout(request);
        return ResponseEntity.ok(response);
    }
}
