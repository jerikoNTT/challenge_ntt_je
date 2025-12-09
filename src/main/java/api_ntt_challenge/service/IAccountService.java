package api_ntt_challenge.service;

import java.util.List;

import api_ntt_challenge.repository.model.Account;

public interface IAccountService {

    Account createAccount(Integer clientId, Account account);
    List<Account> listByClient(Integer clientId);
    Account findByNumber(String number);

}
