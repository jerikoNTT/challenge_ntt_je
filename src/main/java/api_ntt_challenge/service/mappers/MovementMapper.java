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

}
