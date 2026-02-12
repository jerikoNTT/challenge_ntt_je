package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.MovementPersistencePort;
import api_ntt_challenge.exception.ResourceNotFoundException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.policy.MovementPolicy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements IMovementService {

    private final MovementPersistencePort movementPort;
    private final AccountPersistencePort accountPort;
    private final MovementPolicy movementPolicy;

    // constructor removed (generado por Lombok)
    @Override
    @Transactional
    public Movement createMovement(String accountNumber, Movement movement) {
        // Buscar la cuenta; si no existe lanzamos excepción de recurso no encontrado
        Account account = this.accountPort.findByNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        // Calcula el nuevo balance y valida reglas de negocio
        BigDecimal newBalance = movementPolicy.calculateNewBalance(account, movement);
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
