package api_ntt_challenge.service;

import java.util.List;

import api_ntt_challenge.repository.model.Client;

public interface IClientService {

    public Client findForId(Integer id);
    public List<Client> foundAll();
    public void updateForId(Client client);
    public void removeForId(Integer id);
    public void save(Client client);

}
