package bankapp.persistence.mapper;

import bankapp.domain.Movement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovementRowMapper implements RowMapper<Movement> {

    @Override
    public Movement mapRow(ResultSet rs) throws SQLException {
        Movement m = new Movement();
        m.setMovimientoID(rs.getInt("movimientoID"));
        m.setMontoMovimiento(rs.getDouble("montoMovimiento"));
        m.setFechaMovimiento(rs.getString("fechaMovimiento"));
        m.setDescripcionMovimiento(rs.getString("descripcionMovimiento"));
        m.setSaldoPosterior(rs.getDouble("saldoPosterior"));
        m.setCuentaID(rs.getInt("cuentaID"));
        m.setTipoID(rs.getInt("tipoID"));

        // MP-7 CA: "Cada movimiento debe mostrar ... tipo"
        // El repositorio hace JOIN con tipo_movimiento para traer el nombre
        try {
            String tipoNombre = rs.getString("tipoNombre");
            if (tipoNombre != null) m.setTipoNombre(tipoNombre);
        } catch (SQLException ignored) {
            // Si la columna no existe en la query, el fallback de Movement.toString() aplica
        }

        return m;
    }
}
