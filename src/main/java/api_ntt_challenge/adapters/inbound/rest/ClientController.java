package api_ntt_challenge.adapters.inbound.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import api_ntt_challenge.service.IClientService;
import api_ntt_challenge.service.dto.ClientTo;
import api_ntt_challenge.service.mappers.ClientMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Adapter inbound (REST) para Clientes.
 * Mapeo:
 * - Antes (capas): `ClientController` en paquete `api_ntt_challenge.controller` usando JAX-RS
 * - Ahora: `ClientController` (REST adapter) en `adapters.inbound.rest` usando Spring MVC
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientTo> queryForId(@PathVariable Integer id){
        var client = this.clientService.findForId(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ClientMapper.toTo(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientTo>> foundAll() {
        List<ClientTo> list = this.clientService.foundAll()
                .stream()
                .map(ClientMapper::toTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ClientTo> create(@RequestBody ClientTo clientTo) {
        var entity = ClientMapper.toEntity(clientTo);
        this.clientService.save(entity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(location).body(ClientMapper.toTo(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateForId(@PathVariable Integer id, @RequestBody ClientTo clientTo) {
        var client = ClientMapper.toEntity(clientTo);
        client.setId(id);
        this.clientService.updateForId(client);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeForId(@PathVariable Integer id) {
        this.clientService.removeForId(id);
        return ResponseEntity.noContent().build();
    }

}