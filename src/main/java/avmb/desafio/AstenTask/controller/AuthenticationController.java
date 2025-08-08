package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.infra.security.TokenService;
import avmb.desafio.AstenTask.model.user.*;
import avmb.desafio.AstenTask.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO auth) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(auth.email(), auth.password());
        var authentication = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO register) {
        try {
            if (this.repository.findByEmail(register.email()) != null) {
                return ResponseEntity.badRequest().body("Email already registered");
            }
            String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());
            UserRole role = register.role();
            User newUser = new User(register.name(), register.email(), encryptedPassword, role);
            this.repository.save(newUser);
            return ResponseEntity.ok().body("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error registering user: " + e.getMessage());
        }
    }
}
