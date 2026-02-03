package api_ntt_challenge.dto;

/**
 * DTO para la respuesta con el JWT Token
 */
public class TokenResponse {
    private String message;
    private String token;

    public TokenResponse() {}

    public TokenResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
