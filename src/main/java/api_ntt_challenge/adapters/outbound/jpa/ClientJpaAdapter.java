package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;

@Repository
public class ClientJpaAdapter implements ClientPersistencePort {

    private final JpaClientRepository repository;

    public ClientJpaAdapter(JpaClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Client> findByIdentificationNumber(String identificationNumber) {
        return repository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
