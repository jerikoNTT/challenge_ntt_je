package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;

class ClientServiceImplTest {

    @Mock
    private ClientPersistencePort clientPort;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findForId_returnsClient() {
        Client c = new Client();
        c.setId(1);
        when(clientPort.findById(1)).thenReturn(Optional.of(c));

        Client result = clientService.findForId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void foundAll_returnsList() {
        when(clientPort.findAll()).thenReturn(List.of(new Client()));
        List<Client> res = clientService.foundAll();
        assertFalse(res.isEmpty());
    }

    @Test
    void save_callsPort() {
        Client c = new Client();
        clientService.save(c);
        verify(clientPort).save(c);
    }

    @Test
    void remove_callsPort() {
        clientService.removeForId(1);
        verify(clientPort).deleteById(1);
    }
}
