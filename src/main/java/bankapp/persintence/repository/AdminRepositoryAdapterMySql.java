package bankapp.persistence.repository;

import bankapp.domain.Admin;
import bankapp.persistence.mapper.AdminRowMapper;
import bankapp.services.outputport.AdminPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepositoryAdapterMySql implements AdminPersistencePort {

    private final Connection connection;
    private final AdminRowMapper rowMapper;

    public AdminRepositoryAdapterMySql(Connection connection, AdminRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper  = rowMapper;
    }

    @Override
    public Admin saveAdmin(Admin admin) {

        String sql = "INSERT INTO usuario (nombre, email, clave, intentosFallidos, cuentaBloqueada, rol, cargo, permisos, tipoDeCliente) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, admin.getName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());
            ps.setInt(4, 0);
            ps.setBoolean(5, false);
            ps.setString(6, "admin");
            ps.setString(7, admin.getCargo() != null ? admin.getCargo() : "");
            ps.setString(8, admin.getPermissions() != null ? admin.getPermissions() : "");
            ps.setString(9, null);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                admin.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el administrador: " + e.getMessage(), e);
        }

        return admin;
    }

    @Override
    public Admin findAdminByEmail(String email) {

        String sql = "SELECT * FROM usuario WHERE email = ? AND rol = 'admin'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar administrador por email: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Admin> findAllAdmins() {

        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE rol = 'admin'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                admins.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los administradores: " + e.getMessage(), e);
        }

        return admins;
    }
}
