package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import api_ntt_challenge.repository.IAccountRepo;
import api_ntt_challenge.repository.IMovementRepo;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovementServiceImpl implements IMovementService {

    @Inject
    private IMovementRepo movementRepo;

    @Inject
    private IAccountRepo accountRepo;

    @Override
    @Transactional
    public Movement createMovement(String accountNumber, Movement movement) {
        Account account = this.accountRepo.selectByNumber(accountNumber);
        if (account == null) return null;
        if (movement.getValue() == null || movement.getValue().compareTo(BigDecimal.ZERO) <= 0) return null;
        BigDecimal newBalance = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
        if ("WITHDRAW".equalsIgnoreCase(movement.getType())) {
            if (newBalance.compareTo(movement.getValue()) < 0) {
                return null; // insufficient
            }
            newBalance = newBalance.subtract(movement.getValue());
        } else {
            newBalance = newBalance.add(movement.getValue());
        }
        movement.setBalance(newBalance);
        movement.setAccount(account);
        if (movement.getLocalDate() == null) movement.setLocalDate(LocalDateTime.now());
        // persist movement and update account balance
        this.movementRepo.insert(movement);
        account.setBalance(newBalance);
        this.accountRepo.update(account);
        return movement;
    }

    @Override
    public List<Movement> listByAccount(String accountNumber) {
        return this.movementRepo.selectByAccountNumber(accountNumber);
    }

}
