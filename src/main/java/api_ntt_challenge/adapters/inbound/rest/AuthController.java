package api_ntt_challenge.adapters.inbound.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api_ntt_challenge.security.JwtTokenProvider;
import api_ntt_challenge.dto.LoginRequest;
import api_ntt_challenge.dto.TokenResponse;

import java.util.List;

/**
 * Controlador de Autenticación
 * Endpoint para login y obtener JWT Token
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Login: genera un token JWT si las credenciales son válidas
     * 
     * Para este ejemplo, usamos credenciales hardcodeadas.
     * En producción, deberías validar contra una base de datos.
     * 
     * Usuarios de ejemplo:
     * - admin@ntt.com / admin123 → roles: [ADMIN, USER]
     * - user@ntt.com / user123 → roles: [USER]
     * - client@ntt.com / client123 → roles: [CLIENT]
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        
        // Validar credenciales (ESTO ES UN EJEMPLO - en producción usa BD)
        List<String> roles = validateCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        
        if (roles == null) {
            return ResponseEntity.status(401).body(new TokenResponse("Credenciales inválidas", null));
        }

        // Generar token con los roles del usuario
        String token = jwtTokenProvider.generateToken(loginRequest.getEmail(), roles);
        
        return ResponseEntity.ok(new TokenResponse("Login exitoso", token));
    }

    /**
     * Valida credenciales simples (EJEMPLO)
     * En producción, buscaría en BD y compararía contraseña con BCrypt
     */
    private List<String> validateCredentials(String email, String password) {
        // USUARIOS DE PRUEBA (hardcodeados para el ejemplo)
        if ("admin@ntt.com".equals(email) && "admin123".equals(password)) {
            return List.of("ADMIN", "USER");
        }
        if ("user@ntt.com".equals(email) && "user123".equals(password)) {
            return List.of("USER");
        }
        if ("client@ntt.com".equals(email) && "client123".equals(password)) {
            return List.of("CLIENT");
        }
        return null;
    }
}
