package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.MovementPersistencePort;
import api_ntt_challenge.exception.InsufficientFundsException;
import api_ntt_challenge.exception.ResourceNotFoundException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.policy.DefaultMovementPolicy;

class MovementServiceImplTest {

    @Mock
    private MovementPersistencePort movementPort;

    @Mock
    private AccountPersistencePort accountPort;

    private MovementServiceImpl movementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movementService = new MovementServiceImpl(movementPort, accountPort, new DefaultMovementPolicy());
    }

    @Test
    void createMovement_withdrawInsufficient_throwsInsufficientFundsException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        when(accountPort.findByNumber("ACC-1")).thenReturn(Optional.of(account));

        Movement m = new Movement();
        m.setType("WITHDRAW");
        m.setValue(new BigDecimal("200"));

        assertThrows(InsufficientFundsException.class, () -> movementService.createMovement("ACC-1", m));
    }

    @Test
    void createMovement_accountMissing_throwsResourceNotFoundException() {
        when(accountPort.findByNumber("NOPE")).thenReturn(Optional.empty());
        Movement m = new Movement();
        m.setValue(new BigDecimal("10"));
        assertThrows(ResourceNotFoundException.class, () -> movementService.createMovement("NOPE", m));
    }

    @Test
    void createMovement_invalidValue_throwsIllegalArgumentException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        when(accountPort.findByNumber("ACC-2")).thenReturn(Optional.of(account));

        Movement m = new Movement();
        m.setType("DEPOSIT");
        m.setValue(new BigDecimal("0"));

        assertThrows(IllegalArgumentException.class, () -> movementService.createMovement("ACC-2", m));
    }
}