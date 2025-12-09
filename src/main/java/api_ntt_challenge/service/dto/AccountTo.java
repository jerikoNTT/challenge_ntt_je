package api_ntt_challenge.service.dto;

import java.math.BigDecimal;

public class AccountTo {

    private String accType;
    private BigDecimal initialBalance;

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

}
