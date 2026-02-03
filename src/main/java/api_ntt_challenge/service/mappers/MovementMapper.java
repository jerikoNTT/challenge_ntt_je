package api_ntt_challenge.service.mappers;

import java.time.LocalDateTime;

import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.dto.MovementTo;

public class MovementMapper {

    public static Movement toEntity(MovementTo to) {
        if (to == null) return null;
        Movement m = new Movement();
        m.setType(to.getType());
        m.setValue(to.getAmount());
        m.setLocalDate(to.getDate() == null ? LocalDateTime.now() : to.getDate());
        return m;
    }

    public static MovementTo toTo(Movement m) {
        if (m == null) return null;
        MovementTo to = new MovementTo();
        to.setId(m.getId());
        to.setType(m.getType());
        to.setAmount(m.getValue());
        to.setDate(m.getLocalDate());
        to.setBalance(m.getBalance());
        if (m.getAccount() != null) to.setAccountNumber(m.getAccount().getAccNumber());
        return to;
    }

}
