package api_ntt_challenge.application.ports.outbound;

import java.util.Optional;
import java.util.List;

import api_ntt_challenge.repository.model.Client;

public interface ClientPersistencePort {
    Client save(Client client);
    Optional<Client> findById(Integer id);
    Optional<Client> findByIdentificationNumber(String identificationNumber);
    List<Client> findAll();
    void deleteById(Integer id);
}
