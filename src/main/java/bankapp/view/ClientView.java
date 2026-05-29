package bankapp.view;

import bankapp.domain.Client;
import bankapp.services.input.ClientService;
import bankapp.utils.FormValidator;

public class ClientView {

    private final ClientService clientService;

    public ClientView(ClientService clientService) {
        this.clientService = clientService;
    }

    public void createClient() {
        clientService.createClient();
    }

    public void getClientById(int id) {
        Client client = clientService.getClientById(id);
        if (client != null) {
            System.out.println(client);
        }
    }

    public void updateClient() {
        int id = FormValidator.validateInt("Ingrese el ID del cliente a modificar");
        clientService.updateClient(id);
    }

    public void deleteClient() {
        int id = FormValidator.validateInt("Ingrese el ID del cliente a eliminar");
        clientService.deleteClient(id);
    }
}