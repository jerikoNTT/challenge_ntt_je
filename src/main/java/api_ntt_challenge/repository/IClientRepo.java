package api_ntt_challenge.repository;

import java.util.List;

import api_ntt_challenge.repository.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;


public interface IClientRepo {

    public Client selectForId(Integer id);
    public List<Client> selectAll();
    public void refeshForId(Client client);
    public void deletForId(Integer id);
    public void insert(Client client);

}
