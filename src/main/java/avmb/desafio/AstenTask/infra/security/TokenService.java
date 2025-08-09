package avmb.desafio.AstenTask.infra.security;

import avmb.desafio.AstenTask.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    
    private final ConcurrentHashMap<String, Boolean> invalidlistedTokens = new ConcurrentHashMap<>();
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("AstenTask")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException(exception);
        }
    }
    public String validateToken(String token) {
        try {
            if (invalidlistedTokens.containsKey(token)) {
                return "";
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("AstenTask")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
    
    public void invalidateToken(String token) {
        invalidlistedTokens.put(token, true);
    }
    private Instant generateTokenExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
