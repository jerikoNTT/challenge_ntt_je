package api_ntt_challenge.exception;

/**
 * Excepción de dominio que indica que no hay saldo suficiente para realizar un movimiento.
 * Se lanza desde la capa de servicio cuando un retiro supera el balance disponible.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() { super("Saldo no disponible"); }
    public InsufficientFundsException(String message) { super(message); }
}
