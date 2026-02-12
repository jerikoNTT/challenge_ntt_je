package api_ntt_challenge.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementTo {
    private Integer id;
    private String type; // DEPOSIT or WITHDRAW
    private BigDecimal amount;
    private LocalDateTime date;
    private BigDecimal balance; // balance after movement
    private String accountNumber;
}
