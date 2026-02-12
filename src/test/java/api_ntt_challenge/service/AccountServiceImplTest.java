package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.exception.ResourceNotFoundException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;
import api_ntt_challenge.service.support.AccountInitializer;

class AccountServiceImplTest {

    @Mock
    private AccountPersistencePort accountPort;

    @Mock
    private ClientPersistencePort clientPort;

    @Mock
    private AccountInitializer accountInitializer;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountPort, clientPort, accountInitializer);
    }

    @Test
    void createAccount_throwsWhenClientMissing() {
        when(clientPort.findById(1)).thenReturn(Optional.empty());
        Account a = new Account();
        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(1, a));
    }

    @Test
    void createAccount_createsWhenClientExists() {
        Client c = new Client();
        c.setId(1);
        when(clientPort.findById(1)).thenReturn(Optional.of(c));
        doAnswer(invocation -> {
            Account target = invocation.getArgument(0);
            if (target.getBalance() == null) target.setBalance(BigDecimal.ZERO);
            if (target.getAccNumber() == null || target.getAccNumber().isBlank()) target.setAccNumber("ACC-TEST");
            return null;
        }).when(accountInitializer).initialize(any(Account.class));
        when(accountPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Account req = new Account();
        req.setAccNumber(null);
        Account created = accountService.createAccount(1, req);
        assertNotNull(created);
        assertEquals("ACC-TEST", created.getAccNumber());
        assertEquals(BigDecimal.ZERO, created.getBalance());
    }

    @Test
    void listByClient_delegatesToPort() {
        when(accountPort.findByClientId(1)).thenReturn(List.of(new Account()));
        List<Account> res = accountService.listByClient(1);
        assertFalse(res.isEmpty());
    }

    @Test
    void findByNumber_returnsAccount() {
        Account a = new Account();
        when(accountPort.findByNumber("123")).thenReturn(Optional.of(a));
        Account res = accountService.findByNumber("123");
        assertNotNull(res);
    }
}