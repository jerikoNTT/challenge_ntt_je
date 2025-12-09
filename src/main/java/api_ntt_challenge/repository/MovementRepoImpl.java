package api_ntt_challenge.repository;

import java.util.List;

import api_ntt_challenge.repository.model.Movement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class MovementRepoImpl implements IMovementRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Movement selectForId(Integer id) {
        return this.entityManager.find(Movement.class, id);
    }

    @Override
    public List<Movement> selectByAccountNumber(String accountNumber) {
        TypedQuery<Movement> q = this.entityManager.createQuery("SELECT m FROM Movement m WHERE m.account.accNumber = :num ORDER BY m.localDate DESC", Movement.class);
        q.setParameter("num", accountNumber);
        return q.getResultList();
    }

    @Override
    public void insert(Movement movement) {
        this.entityManager.persist(movement);
    }

}
