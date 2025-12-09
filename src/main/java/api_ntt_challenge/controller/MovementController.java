package api_ntt_challenge.controller;

import java.net.URI;
import java.util.List;

import api_ntt_challenge.repository.model.Movement;
import api_ntt_challenge.service.IMovementService;
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

@Path("/accounts/{accountNumber}/movements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovementController {

    @Inject
    private IMovementService movementService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response create(@PathParam("accountNumber") String accountNumber, api_ntt_challenge.service.dto.MovementTo movementTo) {
        Movement entity = api_ntt_challenge.service.mappers.MovementMapper.toEntity(movementTo);
        Movement created = this.movementService.createMovement(accountNumber, entity);
        if (created == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account not found or invalid movement / insufficient funds").build();
        }
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    @GET
    public Response list(@PathParam("accountNumber") String accountNumber) {
        List<Movement> list = this.movementService.listByAccount(accountNumber);
        return Response.ok(list).build();
    }

}
