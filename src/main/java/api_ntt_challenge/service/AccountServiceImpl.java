package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.util.List;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case / Service de Accounts.
 * Cambio de dependencias para arquitectura hexagonal:
 * - Antes (capas): `AccountServiceImpl` dependía directamente de `IAccountRepo` (repositorio)
 * - Ahora (hexagonal): depende del Port `AccountPersistencePort` (inyección del adapter outbound JPA)
 */
@Service
public class AccountServiceImpl implements IAccountService {

    // Nota: este es el Port outbound de persistencia (antes IAccountRepo)
    private final AccountPersistencePort accountPersistencePort;
    private final ClientPersistencePort clientPersistencePort;

    public AccountServiceImpl(AccountPersistencePort accountPersistencePort, ClientPersistencePort clientPersistencePort) {
        this.accountPersistencePort = accountPersistencePort;
        this.clientPersistencePort = clientPersistencePort;
    }

    @Override
    @Transactional
    public Account createAccount(Integer clientId, Account account) {
        Client client = this.clientPersistencePort.selectForId(clientId);
        if (client == null) {
            return null;
        }
        account.setClient(client);
        if (account.getBalance() == null) account.setBalance(BigDecimal.ZERO);
        // generate simple account number if missing
        if (account.getAccNumber() == null || account.getAccNumber().isBlank()) {
            account.setAccNumber("ACC-" + System.currentTimeMillis());
        }
        this.accountPersistencePort.insert(account);
        return account;
    }

    @Override
    public List<Account> listByClient(Integer clientId) {
        return this.accountPersistencePort.selectAllByClientId(clientId);
    }

    @Override
    public Account findByNumber(String number) {
        return this.accountPersistencePort.selectByNumber(number);
    }

}
