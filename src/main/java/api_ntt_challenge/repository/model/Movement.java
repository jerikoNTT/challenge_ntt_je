package api_ntt_challenge.repository.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "move_id")
    private Integer id;

    @Column(name = "move_local_date")
    private LocalDateTime localData;

    @Column(name = "move_type")
    private String type;

    @Column(name = "move_value")
    private Integer value;

    @Column(name = "move_balance")
    private Integer balance;


    ///////getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getLocalData() {
        return localData;
    }

    public void setLocalData(LocalDateTime localData) {
        this.localData = localData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }


    
}
