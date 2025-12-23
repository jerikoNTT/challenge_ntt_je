package api_ntt_challenge.application.ports.outbound;

import java.util.List;

import api_ntt_challenge.repository.model.Movement;

public interface MovementPersistencePort {
    Movement selectForId(Integer id);
    List<Movement> selectByAccountNumber(String accountNumber);
    void insert(Movement movement);
}
