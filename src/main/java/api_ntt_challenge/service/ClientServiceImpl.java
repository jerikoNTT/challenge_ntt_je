package api_ntt_challenge.service;

import java.util.List;

import org.springframework.stereotype.Service;

import api_ntt_challenge.application.ports.outbound.ClientPersistencePort;
import api_ntt_challenge.repository.model.Client;

@Service
public class ClientServiceImpl implements IClientService{

    private final ClientPersistencePort clientPort;

    public ClientServiceImpl(ClientPersistencePort clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public Client findForId(Integer id) {
        return this.clientPort.findById(id).orElse(null);
    }

    @Override
    public List<Client> foundAll() {
        return this.clientPort.findAll();
    }

    @Override
    public void updateForId(Client client) {
        this.clientPort.save(client);
    }

    @Override
    public void removeForId(Integer id) {
        this.clientPort.deleteById(id);
    }

    @Override
    public void save(Client client) {
        this.clientPort.save(client);
    }
}
