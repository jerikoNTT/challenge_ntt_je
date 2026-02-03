package api_ntt_challenge.adapters.outbound.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api_ntt_challenge.repository.model.Client;

@Repository
public interface JpaClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByIdentificationNumber(String identificationNumber);
}
