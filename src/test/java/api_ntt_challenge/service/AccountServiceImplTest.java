package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;

class AccountServiceImplTest {

    @Mock
    private AccountPersistencePort accountPort;

    @Mock
    private ClientPersistencePort clientPort;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_returnsNullWhenClientMissing() {
        when(clientPort.findById(1)).thenReturn(Optional.empty());
        Account a = new Account();
        assertNull(accountService.createAccount(1, a));
    }

    @Test
    void createAccount_createsWhenClientExists() {
        Client c = new Client();
        c.setId(1);
        when(clientPort.findById(1)).thenReturn(Optional.of(c));
        when(accountPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Account req = new Account();
        req.setAccNumber(null);
        Account created = accountService.createAccount(1, req);
        assertNotNull(created);
        assertNotNull(created.getAccNumber());
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