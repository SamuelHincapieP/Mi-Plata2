package bankapp.services.outputport;

import bankapp.domain.Admin;

import java.util.List;

public interface AdminPersistencePort {

    Admin saveAdmin(Admin admin);
    Admin findAdminByEmail(String email);
    List<Admin> findAllAdmins();
}
