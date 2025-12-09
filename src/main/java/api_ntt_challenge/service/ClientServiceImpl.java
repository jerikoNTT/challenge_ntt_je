package api_ntt_challenge.service;

import java.util.List;

import api_ntt_challenge.repository.IClientRepo;
import api_ntt_challenge.repository.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientServiceImpl implements IClientService{

    @Inject
    private IClientRepo clientRepo;

    @Override
    public Client findForId(Integer id) {
        // TODO Auto-generated method stub
        return this.clientRepo.selectForId(id);
    }

    @Override
    public List<Client> foundAll() {
        // TODO Auto-generated method stub
        return this.clientRepo.selectAll();
    }

    @Override
    public void updateForId(Client client) {
        // TODO Auto-generated method stub
        this.clientRepo.refeshForId(client);
    }

    @Override
    public void removeForId(Integer id) {
        // TODO Auto-generated method stub
        this.clientRepo.deletForId(id);
    }

    @Override
    public void save(Client client) {
        // TODO Auto-generated method stub
        this.clientRepo.insert(client);
    }
}
