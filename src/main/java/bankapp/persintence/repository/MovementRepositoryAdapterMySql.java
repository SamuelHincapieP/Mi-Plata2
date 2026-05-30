package bankapp.persistence.repository;

import bankapp.domain.Movement;
import bankapp.persistence.mapper.MovementRowMapper;
import bankapp.services.outputport.MovementPersistencePort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de persistencia para movimientos.
 *
 * Historias de usuario cubiertas:
 *   MP-7  — Consultar movimientos
 *   MP-27 — Filtrar movimientos por fecha (ORDER BY fechaMovimiento DESC)
 */
public class MovementRepositoryAdapterMySql implements MovementPersistencePort {

    private final Connection        connection;
    private final MovementRowMapper rowMapper;

    public MovementRepositoryAdapterMySql(Connection connection, MovementRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper  = rowMapper;
    }

    // MP-5 Consignar / MP-6 Retirar / MP-8 Transferencias / MP-9 Compra TC
    // MP-36 Pagar TC / MP-37 Interés ahorros / MP-38 Cargo sobregiro
    // MP-26 Mensaje de transacción exitosa — guardar cada movimiento
    @Override
    public Movement saveMovement(Movement movement) {

        String sql = "INSERT INTO movimiento (montoMovimiento, fechaMovimiento, descripcionMovimiento, " +
                "saldoPosterior, cuentaID, tipoID) VALUES (?, NOW(), ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, movement.getMontoMovimiento());
            ps.setString(2, movement.getDescripcionMovimiento());
            ps.setDouble(3, movement.getSaldoPosterior());
            ps.setInt(4, movement.getCuentaID());
            ps.setInt(5, movement.getTipoID());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                movement.setMovimientoID(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el movimiento: " + e.getMessage(), e);
        }

        return movement;
    }

    // MP-7 Consultar movimientos / MP-27 Filtrar movimientos por fecha — ORDER BY DESC con JOIN tipo
    @Override
    public List<Movement> findMovementsByCuentaID(int cuentaID) {

        List<Movement> movements = new ArrayList<>();

        String sql = "SELECT m.movimientoID, m.montoMovimiento, " +
                "DATE_FORMAT(m.fechaMovimiento, '%d/%m/%Y %H:%i') AS fechaMovimiento, " +
                "m.descripcionMovimiento, m.saldoPosterior, m.cuentaID, m.tipoID, " +
                "t.nombreTipo AS tipoNombre " +
                "FROM movimiento m " +
                "LEFT JOIN tipo_movimiento t ON m.tipoID = t.tipoID " +
                "WHERE m.cuentaID = ? " +
                "ORDER BY m.fechaMovimiento DESC"; // MP-27 Filtrar movimientos por fecha

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cuentaID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movements.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener movimientos: " + e.getMessage(), e);
        }

        return movements;
    }
}
