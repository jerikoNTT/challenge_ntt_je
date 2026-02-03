package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;

@Service
public class AccountServiceImpl implements IAccountService {

    private final AccountPersistencePort accountPort;
    private final ClientPersistencePort clientPort;

    public AccountServiceImpl(AccountPersistencePort accountPort, ClientPersistencePort clientPort) {
        this.accountPort = accountPort;
        this.clientPort = clientPort;
    }

    @Override
    @Transactional
    public Account createAccount(Integer clientId, Account account) {
        Client client = this.clientPort.findById(clientId).orElse(null);
        if (client == null) {
            return null;
        }
        account.setClient(client);
        if (account.getBalance() == null) account.setBalance(BigDecimal.ZERO);
        // generate simple account number if missing
        if (account.getAccNumber() == null || account.getAccNumber().isBlank()) {
            account.setAccNumber("ACC-" + System.currentTimeMillis());
        }
        return this.accountPort.save(account);
    }

    @Override
    public List<Account> listByClient(Integer clientId) {
        return this.accountPort.findByClientId(clientId);
    }

    @Override
    public Account findByNumber(String number) {
        return this.accountPort.findByNumber(number).orElse(null);
    }

}
