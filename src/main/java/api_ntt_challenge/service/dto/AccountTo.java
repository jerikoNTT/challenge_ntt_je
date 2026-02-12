package api_ntt_challenge.service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTo {
    private Integer id;
    private String accNumber;
    private String accType;
    private BigDecimal balance;
    private String state;
    private Integer clientId;
    private BigDecimal initialBalance;
}
