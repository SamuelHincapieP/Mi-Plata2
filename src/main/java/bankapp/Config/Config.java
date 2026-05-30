package bankapp.config;

import bankapp.persistence.database.DataBaseConnectionMySql;
import bankapp.persistence.mapper.AccountRowMapper;
import bankapp.persistence.mapper.AdminRowMapper;
import bankapp.persistence.mapper.ClientRowMapper;
import bankapp.persistence.mapper.MovementRowMapper;
import bankapp.persistence.mapper.TarjetaCreditoRowMapper;
import bankapp.persistence.repository.AccountRepositoryAdapterMySql;
import bankapp.persistence.repository.AdminRepositoryAdapterMySql;
import bankapp.persistence.repository.ClientRepositoryAdapterMySql;
import bankapp.persistence.repository.CuentaAhorrosRepositoryAdapterMySql;
import bankapp.persistence.repository.CuentaCorrienteRepositoryAdapterMySql;
import bankapp.persistence.repository.MovementRepositoryAdapterMySql;
import bankapp.persistence.repository.TarjetaCreditoRepositoryAdapterMySql;
import bankapp.services.AccountServiceImpl;
import bankapp.services.AdminServiceImpl;
import bankapp.services.AuthService;
import bankapp.services.ClientServiceImpl;
import bankapp.services.input.AccountService;
import bankapp.services.input.ClientAdminService;
import bankapp.services.input.ClientService;
import bankapp.services.outputport.AccountPersistencePort;
import bankapp.services.outputport.AdminPersistencePort;
import bankapp.services.outputport.ClientPersistencePort;
import bankapp.services.outputport.CuentaAhorrosPersistencePort;
import bankapp.services.outputport.CuentaCorrientePersistencePort;
import bankapp.services.outputport.MovementPersistencePort;
import bankapp.services.outputport.TarjetaCreditoPersistencePort;
import bankapp.userinterface.MenuApp;
import bankapp.view.AccountView;
import bankapp.view.AdminView;
import bankapp.view.ClientView;

import java.sql.Connection;

public class Config {

    public static MenuApp createMenuApp() {

        // Patron Simple Factory
        // Cadena: connection → rowMapper → repository → service → view → menuApp

        Connection connection = DataBaseConnectionMySql.getInstance().getConnection();

        // ── Mappers ──────────────────────────────────────────────────────────
        ClientRowMapper         clientRowMapper   = new ClientRowMapper();
        AdminRowMapper          adminRowMapper    = new AdminRowMapper();
        AccountRowMapper        accountRowMapper  = new AccountRowMapper();
        MovementRowMapper       movementRowMapper = new MovementRowMapper();
        TarjetaCreditoRowMapper tcRowMapper       = new TarjetaCreditoRowMapper();

        // ── Repositorios ─────────────────────────────────────────────────────
        ClientPersistencePort         clientRepository    = new ClientRepositoryAdapterMySql(connection, clientRowMapper);
        AdminPersistencePort          adminRepository     = new AdminRepositoryAdapterMySql(connection, adminRowMapper);
        AccountPersistencePort        accountRepository   = new AccountRepositoryAdapterMySql(connection, accountRowMapper);
        MovementPersistencePort       movementRepository  = new MovementRepositoryAdapterMySql(connection, movementRowMapper);
        TarjetaCreditoPersistencePort tarjetaRepository   = new TarjetaCreditoRepositoryAdapterMySql(connection, tcRowMapper);
        CuentaAhorrosPersistencePort   ahorrosRepository   = new CuentaAhorrosRepositoryAdapterMySql(connection);
        CuentaCorrientePersistencePort corrienteRepository = new CuentaCorrienteRepositoryAdapterMySql(connection);

        // ── Servicios ─────────────────────────────────────────────────────────
        ClientService    clientService  = new ClientServiceImpl(clientRepository);
        AccountService   accountService = new AccountServiceImpl(
                accountRepository, clientRepository,
                movementRepository, tarjetaRepository,
                ahorrosRepository, corrienteRepository);
        AuthService      authService    = new AuthService(clientRepository, adminRepository);
        AdminServiceImpl adminService   = new AdminServiceImpl(clientRepository, adminRepository);
        ClientAdminService clientAdminService = adminService;

        // ── Vistas ────────────────────────────────────────────────────────────
        ClientView  clientView  = new ClientView(clientService);
        AccountView accountView = new AccountView(accountService);
        AdminView   adminView   = new AdminView(adminService, clientAdminService);

        return new MenuApp(clientView, adminView, accountView, authService, accountService);
    }
}