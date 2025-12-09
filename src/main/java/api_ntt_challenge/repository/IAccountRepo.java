package api_ntt_challenge.repository;

import java.util.List;

import api_ntt_challenge.repository.model.Account;

public interface IAccountRepo {

    public Account selectForId(Integer id);
    public Account selectByNumber(String number);
    public List<Account> selectAllByClientId(Integer clientId);
    public void insert(Account account);
    public void update(Account account);
    public void delete(Integer id);

}
