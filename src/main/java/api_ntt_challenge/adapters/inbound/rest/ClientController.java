package api_ntt_challenge.adapters.inbound.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api_ntt_challenge.repository.model.Client;
import api_ntt_challenge.service.IClientService;
import api_ntt_challenge.service.dto.ClientTo;
import api_ntt_challenge.service.mappers.ClientMapper;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "USER"})
    public List<ClientTo> list() {
        return clientService.foundAll().stream()
                .map(ClientMapper::toTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<ClientTo> get(@PathVariable Integer id) {
        Client c = clientService.findForId(id);
        return c == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(ClientMapper.toTo(c));
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ClientTo> create(@RequestBody ClientTo clientTo) {
        Client client = ClientMapper.toEntity(clientTo);
        clientService.save(client);
        return ResponseEntity.ok(ClientMapper.toTo(client));
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ClientTo> update(@PathVariable Integer id, @RequestBody ClientTo clientTo) {
        Client client = ClientMapper.toEntity(clientTo);
        client.setId(id);
        clientService.updateForId(client);
        return ResponseEntity.ok(ClientMapper.toTo(client));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clientService.removeForId(id);
        return ResponseEntity.noContent().build();
    }
}
