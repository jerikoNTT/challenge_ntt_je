package api_ntt_challenge.application.ports.outbound;

import java.util.List;

import api_ntt_challenge.repository.model.Client;

/**
 * Port (outbound) para persistencia de Clientes.
 * Mapeo desde la arquitectura anterior (capas):
 * - Antes: `IClientRepo` (repository) / `ClientRepoImpl` (impl)
 * - Ahora: `ClientPersistencePort` (port) y `ClientRepoImpl`/`ClientJpaAdapter` como adapter que lo implementa
 */
public interface ClientPersistencePort {

    Client selectForId(Integer id);

    List<Client> selectAll();

    void refeshForId(Client client);

    void deletForId(Integer id);

    void insert(Client client);

}
