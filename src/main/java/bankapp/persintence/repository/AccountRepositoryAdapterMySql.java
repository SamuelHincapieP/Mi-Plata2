package bankapp.persistence.repository;

import bankapp.domain.Account;
import bankapp.persistence.mapper.AccountRowMapper;
import bankapp.services.outputport.AccountPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryAdapterMySql implements AccountPersistencePort {

    private final Connection connection;
    private final AccountRowMapper rowMapper;

    public AccountRepositoryAdapterMySql(Connection connection, AccountRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper  = rowMapper;
    }

    @Override
    public Account saveAccount(Account account) {

        String sql = "INSERT INTO cuenta (numeroCuenta, saldo, estado, fechaCreacion, usuarioID, tipoCuentaID) " +
                "VALUES (?, ?, ?, NOW(), ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getNumeroCuenta());
            ps.setDouble(2, account.getSaldo());
            ps.setString(3, account.getEstado());
            ps.setInt(4, account.getUsuarioID());
            ps.setInt(5, account.getTipoCuentaID());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                account.setCuentaID(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la cuenta: " + e.getMessage(), e);
        }

        return account;
    }

    @Override
    public Optional<Account> findAccountById(int id) {

        String sql = "SELECT * FROM cuenta WHERE cuentaID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por id: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> findAccountByUsuarioID(int usuarioID) {

        String sql = "SELECT * FROM cuenta WHERE usuarioID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, usuarioID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por usuarioID: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> findAccountByNumeroCuenta(String numeroCuenta) {

        String sql = "SELECT * FROM cuenta WHERE numeroCuenta = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por numero: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Account> findAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM cuenta";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las cuentas: " + e.getMessage(), e);
        }

        return accounts;
    }

    @Override
    public Account updateAccount(Account account) {

        String sql = "UPDATE cuenta SET saldo = ?, estado = ? WHERE cuentaID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, account.getSaldo());
            ps.setString(2, account.getEstado());
            ps.setInt(3, account.getCuentaID());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la cuenta: " + e.getMessage(), e);
        }

        return account;
    }

    @Override
    public void deleteAccount(int id) {

        String sql = "DELETE FROM cuenta WHERE cuentaID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la cuenta: " + e.getMessage(), e);
        }
    }
}
