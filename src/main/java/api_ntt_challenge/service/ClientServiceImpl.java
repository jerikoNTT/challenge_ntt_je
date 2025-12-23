package api_ntt_challenge.service;

import java.util.List;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;
import org.springframework.stereotype.Service;

/**
 * Use Case / Service de Clientes.
 * Cambio de dependencias para arquitectura hexagonal:
 * - Antes (capas): `ClientServiceImpl` dependía directamente de `IClientRepo` (repositorio)
 * - Ahora (hexagonal): depende del Port `ClientPersistencePort` (inyección del adapter outbound JPA)
 *
 * Para facilitar la migración y compatibilidad con el resto del código, mantenemos
 * la interfaz `IClientService` y esta clase la implementa, pero internamente usa
 * `ClientPersistencePort`.
 */
@Service
public class ClientServiceImpl implements IClientService{

    // Nota: este es el Port outbound de persistencia (antes IClientRepo)
    private final ClientPersistencePort clientPersistencePort;

    public ClientServiceImpl(ClientPersistencePort clientPersistencePort) {
        this.clientPersistencePort = clientPersistencePort;
    }

    @Override
    public Client findForId(Integer id) {
        return this.clientPersistencePort.selectForId(id);
    }

    @Override
    public List<Client> foundAll() {
        return this.clientPersistencePort.selectAll();
    }

    @Override
    public void updateForId(Client client) {
        this.clientPersistencePort.refeshForId(client);
    }

    @Override
    public void removeForId(Integer id) {
        this.clientPersistencePort.deletForId(id);
    }

    @Override
    public void save(Client client) {
        this.clientPersistencePort.insert(client);
    }
}
