package api_ntt_challenge.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovementTo {

    private String type; // DEPOSIT or WITHDRAW
    private BigDecimal amount;
    private LocalDateTime date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
