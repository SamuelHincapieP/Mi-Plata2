package bankapp.persistence.mapper;

import bankapp.domain.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs) throws SQLException {

        Client client = new Client();

        client.setId(rs.getInt("usuarioID"));
        client.setName(rs.getString("nombre"));
        client.setEmail(rs.getString("email"));
        client.setPassword(rs.getString("clave"));
        client.setAttemptsFailed(rs.getInt("intentosFallidos"));
        client.setAccountBlocked(rs.getBoolean("cuentaBloqueada"));
        client.setClientType(rs.getString("tipoDeCliente"));

        return client;
    }
}
