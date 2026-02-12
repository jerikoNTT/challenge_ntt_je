package api_ntt_challenge.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.exception.ResourceNotFoundException;
import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Client;
import api_ntt_challenge.service.support.AccountInitializer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountPersistencePort accountPort;
    private final ClientPersistencePort clientPort;
    private final AccountInitializer accountInitializer;

    @Override
    @Transactional
    public Account createAccount(Integer clientId, Account account) {
        Client client = this.clientPort.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        account.setClient(client);
        accountInitializer.initialize(account);
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
