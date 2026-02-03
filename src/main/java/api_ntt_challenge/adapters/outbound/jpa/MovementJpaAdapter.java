package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;
import org.springframework.stereotype.Repository;

import api_ntt_challenge.application.ports.outbound.MovementPersistencePort;
import api_ntt_challenge.repository.model.Movement;

@Repository
public class MovementJpaAdapter implements MovementPersistencePort {

    private final JpaMovementRepository repository;

    public MovementJpaAdapter(JpaMovementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movement save(Movement movement) {
        return repository.save(movement);
    }

    @Override
    public List<Movement> findByAccountNumber(String accountNumber) {
        return repository.findByAccountAccNumber(accountNumber);
    }
}
