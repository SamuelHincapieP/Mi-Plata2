package bankapp.services.outputport;

import bankapp.domain.Client;

import java.util.List;

public interface ClientPersistencePort {

    Client saveClient(Client client);
    List<Client> findAllClients();
    Client findClientById(int id);
    Client findClientByEmail(String email);
    Client updateClient(Client client);
    void deleteClient(int id);
}
