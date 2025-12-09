package api_ntt_challenge.service;

import java.util.List;

import api_ntt_challenge.repository.model.Movement;

public interface IMovementService {

    Movement createMovement(String accountNumber, Movement movement);
    List<Movement> listByAccount(String accountNumber);

}
