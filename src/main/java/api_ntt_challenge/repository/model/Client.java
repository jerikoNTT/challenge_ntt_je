package api_ntt_challenge.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name="client")
@PrimaryKeyJoinColumn(name = "person_id")
public class Client extends Person{

    @Column(name="client_password")
    private String password;

    @Column(name="client_state")
    private String state;

    /////////////getters and setters////////////////

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
