package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.repository.IClientRepo;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    AccountPersistencePort accountPersistencePort;

    @Mock
    api_ntt_challenge.application.ports.outbound.ClientPersistencePort clientPersistencePort;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    void createAccount_success() {
        Client client = new Client();
        client.setId(1);
        when(clientPersistencePort.selectForId(1)).thenReturn(client);

        Account acc = new Account();
        acc.setBalance(BigDecimal.ZERO);
        Account created = accountService.createAccount(1, acc);

        assertNotNull(created);
        verify(accountPersistencePort).insert(acc);
    }

    @Test
    void createAccount_clientNotFound() {
        when(clientPersistencePort.selectForId(99)).thenReturn(null);
        Account acc = new Account();
        Account created = accountService.createAccount(99, acc);
        assertNull(created);
        verify(accountPersistencePort, never()).insert(any());
    }

    @Test
    void listByClient_callsPort() {
        when(accountPersistencePort.selectAllByClientId(1)).thenReturn(List.of(new Account(), new Account()));
        var list = accountService.listByClient(1);
        assertEquals(2, list.size());
        verify(accountPersistencePort).selectAllByClientId(1);
    }

    @Test
    void findByNumber_callsPort() {
        Account a = new Account();
        a.setAccNumber("ACC-1");
        when(accountPersistencePort.selectByNumber("ACC-1")).thenReturn(a);
        var res = accountService.findByNumber("ACC-1");
        assertNotNull(res);
        assertEquals("ACC-1", res.getAccNumber());
    }
}
