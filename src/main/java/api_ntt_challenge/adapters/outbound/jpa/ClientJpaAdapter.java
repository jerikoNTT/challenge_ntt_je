package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Adapter JPA (outbound) para Clientes.
 * Mapeo y notas:
 * - Antes (arquitectura por capas): `IClientRepo` (interfaz) y `ClientRepoImpl` (implementación)
 * - Ahora (arquitectura hexagonal): `ClientPersistencePort` (port) y esta clase es el adapter outbound JPA
 *   que implementa dicho port.
 * Uso: esta clase es detectada por Spring como `@Repository` y se inyecta donde se necesite a través
 * de la interfaz `ClientPersistencePort`.
 */
@Repository
@Transactional
public class ClientJpaAdapter implements ClientPersistencePort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Client selectForId(Integer id) {
        return this.entityManager.find(Client.class, id);
    }

    @Override
    public List<Client> selectAll() {
        TypedQuery<Client> myQuery = this.entityManager.createQuery("SELECT c FROM Client c", Client.class);
        return myQuery.getResultList();
    }

    @Override
    public void refeshForId(Client client) {
        this.entityManager.merge(client);
    }

    @Override
    public void deletForId(Integer id) {
        Client c = this.selectForId(id);
        if (c != null) this.entityManager.remove(c);
    }

    @Override
    public void insert(Client client) {
        this.entityManager.persist(client);
    }
}