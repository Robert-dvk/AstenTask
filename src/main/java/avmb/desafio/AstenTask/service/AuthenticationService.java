package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.InvalidInsertException;
import avmb.desafio.AstenTask.infra.security.TokenService;
import avmb.desafio.AstenTask.model.user.*;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO login(AuthenticationDTO auth) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(auth.email(), auth.password());
        var authentication = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) authentication.getPrincipal());
        return new LoginResponseDTO(token);
    }

    public String register(RegisterDTO register) {
        if (this.userRepository.findByEmail(register.email()) != null) {
            throw new InvalidInsertException("Email already registered");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());
        UserRole role = register.role();
        User newUser = new User(register.name(), register.email(), encryptedPassword, role);
        this.userRepository.save(newUser);
        return "User registered successfully";
    }

    public String logout(HttpServletRequest request) {
        String token = recoverToken(request);
        if (token != null) {
            tokenService.invalidateToken(token);
            return "Logout successful";
        }
        throw new InvalidInsertException("No token found");
    }

    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null) return null;
        return header.replace("Bearer ", "");
    }
}
