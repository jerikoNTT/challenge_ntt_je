package api_ntt_challenge.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientTo {
    private Integer id;
    private String name;
    private String gender;
    private String identificationNumber;
    private String address;
    private String phoneNumber;
    private String password;
    private String state;
}
