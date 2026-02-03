package api_ntt_challenge.adapters.inbound.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.service.IAccountService;
import api_ntt_challenge.service.dto.AccountTo;
import api_ntt_challenge.service.mappers.AccountMapper;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/clients/{clientId}/accounts")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<AccountTo> create(@PathVariable Integer clientId, @RequestBody AccountTo accountTo) {
        Account account = AccountMapper.toEntity(accountTo);
        Account created = accountService.createAccount(clientId, account);
        if (created == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AccountMapper.toTo(created));
    }

    @GetMapping("/clients/{clientId}/accounts")
    @RolesAllowed({"ADMIN", "USER"})
    public List<AccountTo> listByClient(@PathVariable Integer clientId) {
        return accountService.listByClient(clientId).stream()
                .map(AccountMapper::toTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/accounts/{number}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<AccountTo> findByNumber(@PathVariable String number) {
        Account acc = this.accountService.findByNumber(number);
        return acc == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(AccountMapper.toTo(acc));
    }
}
