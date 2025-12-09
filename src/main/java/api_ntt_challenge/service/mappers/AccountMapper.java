package api_ntt_challenge.service.mappers;

import java.math.BigDecimal;

import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.service.dto.AccountTo;

public class AccountMapper {

    public static Account toEntity(AccountTo to) {
        if (to == null) return null;
        Account acc = new Account();
        acc.setAccType(to.getAccType());
        BigDecimal init = to.getInitialBalance() == null ? BigDecimal.ZERO : to.getInitialBalance();
        acc.setBalance(init);
        acc.setState("ACTIVE");
        return acc;
    }

}
