package api_ntt_challenge.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    /**
     * Genera un JWT Token con email y roles
     * 
     * @param email Email del usuario
     * @param roles Lista de roles del usuario (ej: ["ADMIN", "USER"])
     * @return Token JWT generado
     */
    public String generateToken(String email, List<String> roles) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .issuer(jwtIssuer)
                .signWith(key)
                .compact();
    }

    @PostConstruct
    private void validateConfig() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            log.error("JWT secret is not set. Please provide a strong secret via the JWT_SECRET environment variable or configuration.");
            throw new IllegalStateException("Missing jwt.secret configuration");
        }
        if (jwtSecret.length() < 32) {
            log.warn("JWT secret length is less than 32 characters. This is insecure for production environments.");
        }
    }

    /**
     * Valida el token JWT y retorna true si es válido
     * 
     * @param token Token JWT a validar
     * @return true si es válido, false si no
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el email del token JWT
     * 
     * @param token Token JWT
     * @return Email del usuario
     */
    public String getEmailFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Extrae los roles del token JWT
     * 
     * @param token Token JWT
     * @return Lista de roles del usuario
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return (List<String>) claims.get("roles");
    }
}
