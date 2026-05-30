package bankapp.services;

import bankapp.domain.Admin;
import bankapp.domain.Client;
import bankapp.services.input.AdminService;
import bankapp.services.input.ClientAdminService;
import bankapp.services.outputport.AdminPersistencePort;
import bankapp.services.outputport.ClientPersistencePort;
import bankapp.utils.PasswordUtil;

import java.util.List;


public class AdminServiceImpl implements AdminService, ClientAdminService {

    private final ClientPersistencePort clientRepository;
    private final AdminPersistencePort  adminRepository;

    public AdminServiceImpl(ClientPersistencePort clientRepository,
                            AdminPersistencePort adminRepository) {
        this.clientRepository = clientRepository;
        this.adminRepository  = adminRepository;
    }

    // MP-39 Crear administrador — validar correo duplicado, guardar con rol=admin y permisos=FULL
    @Override
    public Admin createAdmin(String name, String email, String password, String cargo) {

        if (clientRepository.findClientByEmail(email) != null) {
            System.out.println("Ese correo ya esta registrado como cliente");
            return null;
        }
        if (adminRepository.findAdminByEmail(email) != null) {
            System.out.println("Ese correo ya esta registrado como administrador");
            return null;
        }

        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(PasswordUtil.hash(password));
        admin.setRol("admin");
        admin.setCargo(cargo);
        admin.setPermissions("FULL");
        admin.setAttemptsFailed(0);
        admin.setAccountBlocked(false);

        return adminRepository.saveAdmin(admin);
    }

    // MP-40 Listar administradores — traer todos los administradores
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAllAdmins();
    }

    // MP-3 Administración de usuarios / MP-22 Listar usuarios registrados — listar clientes
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAllClients();
    }

    // MP-24 Eliminar usuario — eliminar cliente desde el admin
    @Override
    public void deleteClient(int id) {
        clientRepository.deleteClient(id);
    }
}
