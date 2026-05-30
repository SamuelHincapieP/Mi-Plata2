package bankapp.persistence.repository;

import bankapp.domain.CuentaCorriente;
import bankapp.services.outputport.CuentaCorrientePersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * CORRECCIÓN BUG 4: adaptador MySQL para cuenta_corriente.
 */
public class CuentaCorrienteRepositoryAdapterMySql implements CuentaCorrientePersistencePort {

    private final Connection connection;

    public CuentaCorrienteRepositoryAdapterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCuentaCorriente(int cuentaID, double porcentajeSobregiro, double limiteSobregiro) {
        String sql = "INSERT INTO cuenta_corriente (cuentaID, porcentajeSobregiro, limiteSobregiro) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaID);
            ps.setDouble(2, porcentajeSobregiro);
            ps.setDouble(3, limiteSobregiro);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cuenta_corriente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<CuentaCorriente> findByCuentaID(int cuentaID) {
        String sql = "SELECT * FROM cuenta_corriente WHERE cuentaID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cuentaID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CuentaCorriente cc = new CuentaCorriente();
                cc.setCuentaID(rs.getInt("cuentaID"));
                cc.setPorcentajeSobregiro(rs.getDouble("porcentajeSobregiro"));
                cc.setLimiteSobregiro(rs.getDouble("limiteSobregiro"));
                return Optional.of(cc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener cuenta_corriente: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}
