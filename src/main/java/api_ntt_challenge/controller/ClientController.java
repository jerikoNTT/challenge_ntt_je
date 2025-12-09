package api_ntt_challenge.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import api_ntt_challenge.repository.model.Client;
import api_ntt_challenge.service.IClientService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientController {

    @Inject
    private IClientService clientService;

    @GET
    @Path("/query/{id}")
    public Client queryForId(@PathParam("id") Integer id, @Context UriInfo uriInfo){
        return this.clientService.findForId(id);
    }

    @GET
    @Path("")
    public Response foundAll() {
        // TODO Auto-generated method stub
        List<Client> list = this.clientService.foundAll().stream().map(ClientMapper::toTo)
        .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(list).build(); 
    }

    @PUT
    @Path("/{id}")
    public void updateForId(@PathParam("id") Integer id, @RequestBody ClientTo clientTo) {
        // TODO Auto-generated method stub
        var client = ClientMapper.toEntity(clientTo);
        client.setId(id);
        this.clientService.updateForId(client);
    }

    public void removeForId(Integer id) {
        // TODO Auto-generated method stub
        this.clientRepo.deletForId(id);
    }

    public void save(Client client) {
        // TODO Auto-generated method stub
        this.clientRepo.insert(client);
    }

}
