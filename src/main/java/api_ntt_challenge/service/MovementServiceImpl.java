package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.MovementPersistencePort;
import api_ntt_challenge.exception.InsufficientFundsException;
import api_ntt_challenge.exception.ResourceNotFoundException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;

@Service
public class MovementServiceImpl implements IMovementService {

    private final MovementPersistencePort movementPort;
    private final AccountPersistencePort accountPort;

    public MovementServiceImpl(MovementPersistencePort movementPort, AccountPersistencePort accountPort) {
        this.movementPort = movementPort;
        this.accountPort = accountPort;
    }

    @Override
    @Transactional
    public Movement createMovement(String accountNumber, Movement movement) {
        // Buscar la cuenta; si no existe lanzamos excepción de recurso no encontrado
        Account account = this.accountPort.findByNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        // Validación del valor del movimiento; si es nulo o <= 0 lanzamos IllegalArgumentException
        if (movement.getValue() == null || movement.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de movimiento inválido");
        }

        BigDecimal newBalance = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();

        if ("WITHDRAW".equalsIgnoreCase(movement.getType())) {
            // Si no hay saldo suficiente, lanzamos una excepción de dominio InsufficientFundsException
            if (newBalance.compareTo(movement.getValue()) < 0) {
                throw new InsufficientFundsException(); // Mensaje por defecto: "Saldo no disponible"
            }
            newBalance = newBalance.subtract(movement.getValue());
        } else {
            newBalance = newBalance.add(movement.getValue());
        }
        movement.setBalance(newBalance);
        movement.setAccount(account);
        if (movement.getLocalDate() == null) movement.setLocalDate(LocalDateTime.now());
        // persist movement and update account balance
        this.movementPort.save(movement);
        account.setBalance(newBalance);
        this.accountPort.save(account);
        return movement;
    }

    @Override
    public List<Movement> listByAccount(String accountNumber) {
        return this.movementPort.findByAccountNumber(accountNumber);
    }

}
