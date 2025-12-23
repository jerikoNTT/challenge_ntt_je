package api_ntt_challenge.adapters.inbound.rest;

import java.net.URI;
import java.util.List;

import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.IMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Adapter inbound (REST) para Movements.
 */
@RestController
@RequestMapping("/accounts/{accountNumber}/movements")
public class MovementController {

    private final IMovementService movementService;

    public MovementController(IMovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<Movement> create(@PathVariable("accountNumber") String accountNumber, @RequestBody api_ntt_challenge.service.dto.MovementTo movementTo) {
        Movement entity = api_ntt_challenge.service.mappers.MovementMapper.toEntity(movementTo);
        Movement created = this.movementService.createMovement(accountNumber, entity);
        if (created == null) {
            return ResponseEntity.badRequest().body(null);
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Movement>> list(@PathVariable("accountNumber") String accountNumber) {
        List<Movement> list = this.movementService.listByAccount(accountNumber);
        return ResponseEntity.ok(list);
    }

}