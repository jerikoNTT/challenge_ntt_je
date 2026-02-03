package api_ntt_challenge.exception;

/**
 * Excepción que representa un recurso no encontrado (p. ej. cuenta inexistente).
 * Se usa para devolver 404 o 400 según el manejador global (aquí se mapeará a 400 en el ejemplo).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() { super("Recurso no encontrado"); }
    public ResourceNotFoundException(String message) { super(message); }
}
