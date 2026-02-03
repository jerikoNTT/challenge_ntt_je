package api_ntt_challenge.application.ports.outbound;

import java.util.Optional;
import java.util.List;

import api_ntt_challenge.repository.model.Account;

public interface AccountPersistencePort {
    Account save(Account account);
    Optional<Account> findById(Integer id);
    Optional<Account> findByNumber(String accountNumber);
    List<Account> findByClientId(Integer clientId);
}
