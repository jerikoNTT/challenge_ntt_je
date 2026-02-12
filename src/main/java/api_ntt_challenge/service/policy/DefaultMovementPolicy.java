package api_ntt_challenge.service.policy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import api_ntt_challenge.exception.InsufficientFundsException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;

@Component
public class DefaultMovementPolicy implements MovementPolicy {

    @Override
    public BigDecimal calculateNewBalance(Account account, Movement movement) {
        validateMovement(movement);

        BigDecimal newBalance = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();

        if ("WITHDRAW".equalsIgnoreCase(movement.getType())) {
            if (newBalance.compareTo(movement.getValue()) < 0) {
                throw new InsufficientFundsException();
            }
            return newBalance.subtract(movement.getValue());
        }

        return newBalance.add(movement.getValue());
    }

    private void validateMovement(Movement movement) {
        if (movement.getValue() == null || movement.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de movimiento invalido");
        }
    }
}
