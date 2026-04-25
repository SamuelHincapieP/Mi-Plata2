package bankapp.service;

import bankapp.domain.Client;

import java.util.Optional;

public interface ClientService {

    // Estos metodos abstractos se configuran en el contrato
    public Client createClient();
    public Client getClientById(int id);
    public Optional<Client> getClientByEmail(String email);
    public Client updateClient(int id);
    public void deleteClient(int id);
}
