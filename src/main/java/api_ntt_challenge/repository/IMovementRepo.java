package api_ntt_challenge.repository;

import java.util.List;

import api_ntt_challenge.repository.model.Movement;

public interface IMovementRepo {

    public Movement selectForId(Integer id);
    public List<Movement> selectByAccountNumber(String accountNumber);
    public void insert(Movement movement);

}
