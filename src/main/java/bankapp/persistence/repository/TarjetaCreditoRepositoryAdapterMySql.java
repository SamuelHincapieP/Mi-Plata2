package bankapp.persistence.repository;

import bankapp.domain.TarjetaCredito;
import bankapp.persistence.mapper.TarjetaCreditoRowMapper;
import bankapp.services.outputport.TarjetaCreditoPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TarjetaCreditoRepositoryAdapterMySql implements TarjetaCreditoPersistencePort {

    private final Connection connection;
    private final TarjetaCreditoRowMapper rowMapper;

    public TarjetaCreditoRepositoryAdapterMySql(Connection connection, TarjetaCreditoRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper  = rowMapper;
    }

    @Override
    public TarjetaCredito saveTarjetaCredito(TarjetaCredito tc) {

        String sql = "INSERT INTO tarjeta_credito (cuentaID, cupo, deuda, numeroCuotas, tasaMensual) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, tc.getCuentaID());
            ps.setDouble(2, tc.getCupo());
            ps.setDouble(3, tc.getDeuda());
            ps.setInt(4, tc.getNumeroCuotas());
            ps.setDouble(5, tc.getTasaMensual());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar tarjeta de credito: " + e.getMessage(), e);
        }

        return tc;
    }

    @Override
    public Optional<TarjetaCredito> findTarjetaByCuentaID(int cuentaID) {

        String sql = "SELECT * FROM tarjeta_credito WHERE cuentaID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cuentaID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tarjeta de credito: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public TarjetaCredito updateTarjetaCredito(TarjetaCredito tc) {

        String sql = "UPDATE tarjeta_credito SET deuda = ?, numeroCuotas = ?, tasaMensual = ? WHERE cuentaID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, tc.getDeuda());
            ps.setInt(2, tc.getNumeroCuotas());
            ps.setDouble(3, tc.getTasaMensual());
            ps.setInt(4, tc.getCuentaID());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tarjeta de credito: " + e.getMessage(), e);
        }

        return tc;
    }
}
