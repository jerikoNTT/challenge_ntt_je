package api_ntt_challenge.service.mappers;

import api_ntt_challenge.repository.model.Client;
import api_ntt_challenge.service.dto.ClientTo;

public class ClientMapper {

    public static ClientTo toTo(Client client) {
        if (client == null) return null;
        ClientTo to = new ClientTo();
        to.setId(client.getId());
        to.setName(client.getName());
        to.setGender(client.getGender());
        to.setIdentificationNumber(client.getIdentificationNumber());
        to.setAddress(client.getAddress());
        to.setPhoneNumber(client.getPhoneNumber());
        to.setPassword(client.getPassword());
        to.setState(client.getState());
        return to;
    }

    public static Client toEntity(ClientTo to) {
        if (to == null) return null;
        Client client = new Client();
        client.setId(to.getId());
        client.setName(to.getName());
        client.setGender(to.getGender());
        client.setIdentificationNumber(to.getIdentificationNumber());
        client.setAddress(to.getAddress());
        client.setPhoneNumber(to.getPhoneNumber());
        client.setPassword(to.getPassword());
        client.setState(to.getState());
        return client;
    }

}
