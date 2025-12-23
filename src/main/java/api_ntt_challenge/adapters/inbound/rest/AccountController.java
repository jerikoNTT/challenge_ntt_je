package api_ntt_challenge.adapters.inbound.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.service.IAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Adapter inbound (REST) para Accounts.
 * Mapeo:
 * - Antes (capas): `AccountController` en `api_ntt_challenge.controller` con JAX-RS
 * - Ahora: Spring MVC controller en `adapters.inbound.rest`
 */
@RestController
@RequestMapping("/clients/{clientId}/accounts")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> create(@PathVariable("clientId") Integer clientId, @RequestBody api_ntt_challenge.service.dto.AccountTo accountTo) {
        Account entity = api_ntt_challenge.service.mappers.AccountMapper.toEntity(accountTo);
        Account created = this.accountService.createAccount(clientId, entity);
        if (created == null) {
            return ResponseEntity.notFound().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{acc}")
                .buildAndExpand(created.getAccNumber()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Account>> list(@PathVariable("clientId") Integer clientId) {
        List<Account> list = this.accountService.listByClient(clientId);
        return ResponseEntity.ok(list.stream().map(a -> {
            // simple map to a response object
            return a;
        }).collect(Collectors.toList()));
    }

}