package api_ntt_challenge.service;

import java.math.BigDecimal;
import java.util.List;

import api_ntt_challenge.repository.IAccountRepo;
import api_ntt_challenge.repository.IClientRepo;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AccountServiceImpl implements IAccountService {

    @Inject
    private IAccountRepo accountRepo;

    @Inject
    private IClientRepo clientRepo;

    @Override
    @Transactional
    public Account createAccount(Integer clientId, Account account) {
        Client client = this.clientRepo.selectForId(clientId);
        if (client == null) {
            return null;
        }
        account.setClient(client);
        if (account.getBalance() == null) account.setBalance(BigDecimal.ZERO);
        // generate simple account number if missing
        if (account.getAccNumber() == null || account.getAccNumber().isBlank()) {
            account.setAccNumber("ACC-" + System.currentTimeMillis());
        }
        this.accountRepo.insert(account);
        return account;
    }

    @Override
    public List<Account> listByClient(Integer clientId) {
        return this.accountRepo.selectAllByClientId(clientId);
    }

    @Override
    public Account findByNumber(String number) {
        return this.accountRepo.selectByNumber(number);
    }

}
