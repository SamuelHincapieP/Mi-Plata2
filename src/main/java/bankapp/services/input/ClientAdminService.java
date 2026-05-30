package bankapp.services.input;

import bankapp.domain.Client;

import java.util.List;

public interface ClientAdminService {

    List<Client> getAllClients();
    void deleteClient(int id);
}
