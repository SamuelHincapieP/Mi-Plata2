package bankapp.persistence.mapper;

import bankapp.domain.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRowMapper implements RowMapper<Admin> {

    @Override
    public Admin mapRow(ResultSet rs) throws SQLException {

        Admin admin = new Admin();

        admin.setId(rs.getInt("usuarioID"));
        admin.setName(rs.getString("nombre"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("clave"));
        admin.setAttemptsFailed(rs.getInt("intentosFallidos"));
        admin.setAccountBlocked(rs.getBoolean("cuentaBloqueada"));
        admin.setRol(rs.getString("rol"));
        admin.setCargo(rs.getString("cargo"));
        admin.setPermissions(rs.getString("permisos"));

        return admin;
    }
}
