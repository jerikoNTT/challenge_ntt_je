package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import api_ntt_challenge.application.ports.outbound.AccountPersistencePort;
import api_ntt_challenge.repository.model.Account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Adapter JPA (outbound) para Accounts.
 * - Antes: `IAccountRepo` + `AccountRepoImpl`
 * - Ahora: `AccountPersistencePort` (port) y este adapter JPA lo implementa.
 */
@Repository
@Transactional
public class AccountJpaAdapter implements AccountPersistencePort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account selectForId(Integer id) {
        return this.entityManager.find(Account.class, id);
    }

    @Override
    public Account selectByNumber(String number) {
        TypedQuery<Account> q = this.entityManager.createQuery("SELECT a FROM Account a WHERE a.accNumber = :num", Account.class);
        q.setParameter("num", number);
        var res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    @Override
    public List<Account> selectAllByClientId(Integer clientId) {
        TypedQuery<Account> q = this.entityManager.createQuery("SELECT a FROM Account a WHERE a.client.id = :cid", Account.class);
        q.setParameter("cid", clientId);
        return q.getResultList();
    }

    @Override
    public void insert(Account account) {
        this.entityManager.persist(account);
    }

    @Override
    public void update(Account account) {
        this.entityManager.merge(account);
    }

    @Override
    public void delete(Integer id) {
        Account a = selectForId(id);
        if (a != null) this.entityManager.remove(a);
    }
}