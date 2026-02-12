package api_ntt_challenge.service.support;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import api_ntt_challenge.repository.model.Account;

@Component
public class DefaultAccountInitializer implements AccountInitializer {

    private final AccountNumberGenerator numberGenerator;

    public DefaultAccountInitializer(AccountNumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    @Override
    public void initialize(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        if (account.getAccNumber() == null || account.getAccNumber().isBlank()) {
            account.setAccNumber(numberGenerator.generate());
        }
    }
}
