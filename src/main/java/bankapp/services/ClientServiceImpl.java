package bankapp.services;

import bankapp.domain.Client;
import bankapp.services.input.ClientService;
import bankapp.services.outputport.ClientPersistencePort;
import bankapp.utils.PasswordUtil;

import java.util.Optional;

/**
 * Lógica de negocio para clientes.
 *
 * Historias de usuario cubiertas:
 *   MP-1  — Registro de cliente
 *   MP-3  — Administración de usuarios
 *   MP-10 — Editar perfil
 *   MP-11 — Cambio de contraseña
 *   MP-21 — Recuperar contraseña
 *   MP-23 — Buscar usuario por documento
 *   MP-24 — Eliminar usuario
 *   MP-25 — Actualizar información personal
 */
public class ClientServiceImpl implements ClientService {

    private final ClientPersistencePort clientRepository;

    public ClientServiceImpl(ClientPersistencePort clientRepository) {
        this.clientRepository = clientRepository;
    }

    // MP-1 Registro de cliente — crear cliente con intentosFallidos=0 y cuentaBloqueada=false
    @Override
    public Client createClient(String name, String email, String password, String clientType) {
        if (clientRepository.findClientByEmail(email) != null) {
            System.out.println("Ese correo ya esta registrado. Ingrese uno diferente.");
            return null;
        }
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setPassword(PasswordUtil.hash(password));
        client.setAttemptsFailed(0);
        client.setAccountBlocked(false);
        client.setClientType(clientType);
        return clientRepository.saveClient(client);
    }

    // MP-23 Buscar usuario por documento — buscar cliente por ID
    @Override
    public Client getClientById(int id) {
        return clientRepository.findClientById(id);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return Optional.ofNullable(clientRepository.findClientByEmail(email));
    }

    // MP-10 Editar perfil / MP-11 Cambio de contraseña / MP-21 Recuperar contraseña
    // MP-25 Actualizar información personal — actualizar campos del cliente


    @Override
    public Client updateClient(int id, String name, String email, String password, String clientType) {
        Client client = clientRepository.findClientById(id);
        
        if (client != null) {
            client.setName(name);
            client.setEmail(email);
            client.setPassword(PasswordUtil.hash(password));
            client.setClientType(clientType);
            return clientRepository.updateClient(client);

        } else {


            System.out.println("Cliente no encontrado con ID: " + id);
            return null;



        }
    }

    // MP-24 Eliminar usuario — eliminar cliente de la BD
    @Override
    public void deleteClient(int id) {
        clientRepository.deleteClient(id);
    }
}
