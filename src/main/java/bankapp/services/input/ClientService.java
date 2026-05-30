package bankapp.services.input;

import bankapp.domain.Client;

import java.util.Optional;

public interface ClientService {

    // MP-1: Registro — recibe todos los datos desde la View (patron del profe)
    Client createClient(String name, String email, String password, String clientType);

    Client getClientById(int id);
    Optional<Client> getClientByEmail(String email);

    // MP-3: Actualizar — recibe el objeto ya modificado
    Client updateClient(int id, String name, String email, String password, String clientType);

    void deleteClient(int id);
}
