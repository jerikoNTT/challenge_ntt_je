package api_ntt_challenge.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import api_ntt_challenge.service.IClientService;
import api_ntt_challenge.service.dto.ClientTo;
import api_ntt_challenge.service.mappers.ClientMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientController {

    @Inject
    private IClientService clientService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    public Response queryForId(@PathParam("id") Integer id){
        var client = this.clientService.findForId(id);
        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ClientMapper.toTo(client)).build();
    }

    @GET
    @Path("")
    public Response foundAll() {
        List<ClientTo> list = this.clientService.foundAll()
                .stream()
                .map(ClientMapper::toTo)
                .collect(Collectors.toList());
        return Response.ok(list).build();
    }

    @POST
    @Path("")
    public Response create(ClientTo clientTo) {
        var entity = ClientMapper.toEntity(clientTo);
        this.clientService.save(entity);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(entity.getId())).build();
        return Response.created(location).entity(ClientMapper.toTo(entity)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateForId(@PathParam("id") Integer id, ClientTo clientTo) {
        var client = ClientMapper.toEntity(clientTo);
        client.setId(id);
        this.clientService.updateForId(client);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeForId(@PathParam("id") Integer id) {
        this.clientService.removeForId(id);
        return Response.noContent().build();
    }

}
