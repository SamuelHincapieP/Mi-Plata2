package bankapp.persistence.repository;

import bankapp.domain.Client;
import bankapp.persistence.mapper.ClientRowMapper;
import bankapp.services.outputport.ClientPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryAdapterMySql implements ClientPersistencePort {

    private final Connection connection;
    private final ClientRowMapper rowMapper;

    public ClientRepositoryAdapterMySql(Connection connection, ClientRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper  = rowMapper;
    }

    @Override
    public Client saveClient(Client client) {

        String sql = "INSERT INTO usuario (nombre, email, clave, intentosFallidos, cuentaBloqueada, rol, cargo, permisos, tipoDeCliente) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            setClientParams(ps, client);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                client.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente: " + e.getMessage(), e);
        }

        return client;
    }

    @Override
    public List<Client> findAllClients() {

        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE rol = 'cliente'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clients.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los clientes: " + e.getMessage(), e);
        }

        return clients;
    }

    @Override
    public Client findClientById(int id) {

        String sql = "SELECT * FROM usuario WHERE usuarioID = ? AND rol = 'cliente'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por id: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Client findClientByEmail(String email) {

        String sql = "SELECT * FROM usuario WHERE email = ? AND rol = 'cliente'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por email: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Client updateClient(Client client) {

        String sql = "UPDATE usuario SET nombre = ?, email = ?, clave = ?, intentosFallidos = ?, " +
                "cuentaBloqueada = ?, tipoDeCliente = ? WHERE usuarioID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPassword());
            ps.setInt(4, client.getAttemptsFailed());
            ps.setBoolean(5, client.isAccountBlocked());
            ps.setString(6, client.getClientType());
            ps.setInt(7, client.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage(), e);
        }

        return client;
    }

    @Override
    public void deleteClient(int id) {

        String sql = "DELETE FROM usuario WHERE usuarioID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente: " + e.getMessage(), e);
        }
    }

    // ── Helper privado ─────────────────────────────────────────────────────
    private void setClientParams(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getName());
        ps.setString(2, client.getEmail());
        ps.setString(3, client.getPassword());
        ps.setInt(4, client.getAttemptsFailed());
        ps.setBoolean(5, client.isAccountBlocked());
        ps.setString(6, "cliente");
        ps.setString(7, "");
        ps.setString(8, "");
        ps.setString(9, client.getClientType());
    }
}
