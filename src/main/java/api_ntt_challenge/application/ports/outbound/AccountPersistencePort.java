package api_ntt_challenge.application.ports.outbound;

import java.util.List;

import api_ntt_challenge.repository.model.Account;

/**
 * Port (outbound) para persistencia de Accounts.
 * Mapeo desde la arquitectura anterior (capas):
 * - Antes: `IAccountRepo` (repository) / `AccountRepoImpl` (impl)
 * - Ahora: `AccountPersistencePort` (port) y `AccountRepoImpl`/`AccountJpaAdapter` como adapter que lo implementa
 */
public interface AccountPersistencePort {

    Account selectForId(Integer id);

    Account selectByNumber(String number);

    List<Account> selectAllByClientId(Integer clientId);

    void insert(Account account);

    void update(Account account);

    void delete(Integer id);

}
