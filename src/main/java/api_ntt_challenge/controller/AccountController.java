package api_ntt_challenge.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import api_ntt_challenge.repository.model.Account;
import api_ntt_challenge.service.IAccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/clients/{clientId}/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    private IAccountService accountService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response create(@PathParam("clientId") Integer clientId, api_ntt_challenge.service.dto.AccountTo accountTo) {
        Account entity = api_ntt_challenge.service.mappers.AccountMapper.toEntity(accountTo);
        Account created = this.accountService.createAccount(clientId, entity);
        if (created == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Client not found or invalid data").build();
        }
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getAccNumber()).build();
        return Response.created(location).entity(created).build();
    }

    @GET
    public Response list(@PathParam("clientId") Integer clientId) {
        List<Account> list = this.accountService.listByClient(clientId);
        return Response.ok(list.stream().map(a -> {
            // simple map to a response object
            return a;
        }).collect(Collectors.toList())).build();
    }

}
