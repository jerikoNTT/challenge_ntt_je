package api_ntt_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    ClientPersistencePort clientPersistencePort;

    @InjectMocks
    ClientServiceImpl clientService;

    @Test
    void findForId_found() {
        Client c = new Client();
        c.setId(1);
        c.setName("John");
        when(clientPersistencePort.selectForId(1)).thenReturn(c);

        Client res = clientService.findForId(1);
        assertNotNull(res);
        assertEquals(1, res.getId());
        verify(clientPersistencePort).selectForId(1);
    }

    @Test
    void findForId_notFound() {
        when(clientPersistencePort.selectForId(5)).thenReturn(null);
        Client res = clientService.findForId(5);
        assertNull(res);
        verify(clientPersistencePort).selectForId(5);
    }

    @Test
    void foundAll_returnsList() {
        when(clientPersistencePort.selectAll()).thenReturn(List.of(new Client(), new Client()));
        var list = clientService.foundAll();
        assertEquals(2, list.size());
        verify(clientPersistencePort).selectAll();
    }

    @Test
    void save_callsInsert() {
        Client c = new Client();
        clientService.save(c);
        verify(clientPersistencePort).insert(c);
    }

    @Test
    void update_callsRefresh() {
        Client c = new Client();
        clientService.updateForId(c);
        verify(clientPersistencePort).refeshForId(c);
    }

    @Test
    void remove_callsDelete() {
        clientService.removeForId(10);
        verify(clientPersistencePort).deletForId(10);
    }
}
