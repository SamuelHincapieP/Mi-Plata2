package bankapp.persistence.mapper;

import bankapp.domain.TarjetaCredito;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TarjetaCreditoRowMapper implements bankapp.persistence.mapper.RowMapper<TarjetaCredito> {

    @Override
    public TarjetaCredito mapRow(ResultSet rs) throws SQLException {

        TarjetaCredito tc = new TarjetaCredito();

        tc.setCuentaID(rs.getInt("cuentaID"));
        tc.setCupo(rs.getDouble("cupo"));
        tc.setDeuda(rs.getDouble("deuda"));
        tc.setNumeroCuotas(rs.getInt("numeroCuotas"));
        tc.setTasaMensual(rs.getDouble("tasaMensual"));

        return tc;
    }
}
