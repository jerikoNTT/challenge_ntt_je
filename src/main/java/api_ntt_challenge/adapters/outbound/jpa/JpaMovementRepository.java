package api_ntt_challenge.adapters.outbound.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api_ntt_challenge.repository.model.Movement;

@Repository
public interface JpaMovementRepository extends JpaRepository<Movement, Integer> {
    List<Movement> findByAccountAccNumber(String accNumber);
}
