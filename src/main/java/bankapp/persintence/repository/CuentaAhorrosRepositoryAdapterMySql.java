package bankapp.persistence.repository;

import bankapp.domain.CuentaAhorros;
import bankapp.services.outputport.CuentaAhorrosPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CuentaAhorrosRepositoryAdapterMySql implements CuentaAhorrosPersistencePort {

    private final Connection connection;

    public CuentaAhorrosRepositoryAdapterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCuentaAhorros(int cuentaID, double tasaInteres) {
        String sql = "INSERT INTO cuenta_ahorros (cuentaID, tasaInteres) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaID);
            ps.setDouble(2, tasaInteres);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cuenta_ahorros: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<CuentaAhorros> findByCuentaID(int cuentaID) {
        String sql = "SELECT * FROM cuenta_ahorros WHERE cuentaID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CuentaAhorros ca = new CuentaAhorros();
                ca.setCuentaID(rs.getInt("cuentaID"));
                ca.setTasaInteres(rs.getDouble("tasaInteres"));
                return Optional.of(ca);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener cuenta_ahorros: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}
