package api_ntt_challenge.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="acco_id")
    private Integer id;

    @Column(name="acco_number")
    private String accNUmber;    //numer account

    @Column(name="acco_Type")
    private String accType;      //type account

    @Column(name="acco_balance_Initial")
    private Integer balanceInitial;  

    @Column(name="acco_state")
    private String state;


    ////////// getters and setters ///////
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccNUmber() {
        return accNUmber;
    }

    public void setAccNUmber(String accNUmber) {
        this.accNUmber = accNUmber;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public Integer getBalanceInitial() {
        return balanceInitial;
    }

    public void setBalanceInitial(Integer balanceInitial) {
        this.balanceInitial = balanceInitial;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
