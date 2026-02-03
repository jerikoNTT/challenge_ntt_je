package api_ntt_challenge.adapters.inbound.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.IMovementService;
import api_ntt_challenge.service.dto.MovementTo;
import api_ntt_challenge.service.mappers.MovementMapper;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping
public class MovementController {

    private final IMovementService movementService;

    public MovementController(IMovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping("/accounts/{accountNumber}/movements")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<MovementTo> create(@PathVariable String accountNumber, @RequestBody MovementTo movementTo) {
        // Dejar que las excepciones (p. ej. InsufficientFundsException) se propaguen y
        // sean manejadas por el GlobalExceptionHandler para devolver JSON consistente.
        Movement movement = MovementMapper.toEntity(movementTo);
        Movement created = movementService.createMovement(accountNumber, movement);
        return ResponseEntity.ok(MovementMapper.toTo(created));
    }

    @GetMapping("/accounts/{accountNumber}/movements")
    @RolesAllowed({"ADMIN", "USER"})
    public List<MovementTo> list(@PathVariable String accountNumber) {
        return movementService.listByAccount(accountNumber).stream()
                .map(MovementMapper::toTo)
                .collect(Collectors.toList());
    }
}
