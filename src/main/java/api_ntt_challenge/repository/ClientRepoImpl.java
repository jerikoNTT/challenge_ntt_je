package api_ntt_challenge.repository;

import java.util.List;

import api_ntt_challenge.repository.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@ApplicationScoped//ambio que los datos que se maneje se mantienen en la aplicacion
@Transactional //manejo de transacciones
public class ClientRepoImpl implements IClientRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Client selectForId(Integer id) {
        // TODO Auto-generated method stub
        return this.entityManager.find(Client.class, id);

    }

    @Override
    public List<Client> selectAll() {
        // TODO Auto-generated method stub
        TypedQuery<Client> myQuery = this.entityManager.createNamedQuery("SELECT c FROM Client c", Client.class);
        return myQuery.getResultList();
    }

    @Override
    public void refeshForId(Client client) {
        // TODO Auto-generated method stub
        this.entityManager.merge(client);
    }

    @Override
    public void deletForId(Integer id) {
        // TODO Auto-generated method stub
        this.entityManager.remove(this.selectForId(id));
    }

    @Override
    public void insert(Client client) {
        // TODO Auto-generated method stub
        this.entityManager.persist(client);
    }


}
