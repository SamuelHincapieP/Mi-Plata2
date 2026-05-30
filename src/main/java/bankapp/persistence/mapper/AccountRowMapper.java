package bankapp.persistence.mapper;

import bankapp.domain.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs) throws SQLException {

        Account account = new Account();

        account.setCuentaID(rs.getInt("cuentaID"));
        account.setNumeroCuenta(rs.getString("numeroCuenta"));
        account.setSaldo(rs.getDouble("saldo"));
        account.setEstado(rs.getString("estado"));
        account.setFechaCreacion(rs.getString("fechaCreacion"));
        account.setUsuarioID(rs.getInt("usuarioID"));
        account.setTipoCuentaID(rs.getInt("tipoCuentaID"));

        return account;
    }
}
