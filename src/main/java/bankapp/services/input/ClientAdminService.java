package bankapp.services.input;

import bankapp.domain.Client;

import java.util.List;

public interface ClientAdminService {

    public List<Client> getAllClients();
    public void deleteClient(int id);
}
