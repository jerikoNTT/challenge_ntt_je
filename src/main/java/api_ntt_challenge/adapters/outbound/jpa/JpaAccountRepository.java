package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api_ntt_challenge.repository.model.Account;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccNumber(String accNumber);
    List<Account> findByClientId(Integer clientId);
}
