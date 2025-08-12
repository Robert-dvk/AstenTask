package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.user.*;
import avmb.desafio.AstenTask.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para login, registro e logout")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Login do usuário", description = "Autentica o usuário e retorna token JWT")
    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO auth) {
        var loginResponse = authenticationService.login(auth);
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Registro de novo usuário", description = "Cadastra um novo usuário no sistema")
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO register) {
        String response = authenticationService.register(register);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Logout do usuário", description = "Encerra a sessão ou invalida token JWT")
    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String response = authenticationService.logout(request);
        return ResponseEntity.ok(response);
    }
}
