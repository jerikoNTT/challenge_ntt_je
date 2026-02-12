package api_ntt_challenge.service.policy;

import java.math.BigDecimal;

import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.repository.model.Movement;

public interface MovementPolicy {
    BigDecimal calculateNewBalance(Account account, Movement movement);
}
