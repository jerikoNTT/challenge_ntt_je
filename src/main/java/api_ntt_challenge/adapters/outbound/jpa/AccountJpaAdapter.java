package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.repository.model.Account;

@Repository
public class AccountJpaAdapter implements AccountPersistencePort {

    private final JpaAccountRepository repository;

    public AccountJpaAdapter(JpaAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Account> findByNumber(String accountNumber) {
        return repository.findByAccNumber(accountNumber);
    }

    @Override
    public List<Account> findByClientId(Integer clientId) {
        return repository.findByClientId(clientId);
    }
}
