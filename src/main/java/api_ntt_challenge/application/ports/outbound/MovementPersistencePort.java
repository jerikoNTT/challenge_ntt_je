package api_ntt_challenge.application.ports.outbound;

import java.util.List;

import api_ntt_challenge.repository.model.Movement;

public interface MovementPersistencePort {
    Movement save(Movement movement);
    List<Movement> findByAccountNumber(String accountNumber);
}
