package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.application.ports.outbound.MovementPersistencePort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Adapter JPA (outbound) para Movements.
 * - Nota: MovementPersistencePort is not previously created; create here for parity if needed.
 */
@Repository
@Transactional
public class MovementJpaAdapter implements MovementPersistencePort {

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