package bankapp.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionMySql {

    private static DataBaseConnectionMySql instance;
    private Connection connection;

    private static final String URL      = "jdbc:mysql://localhost:3306/mi_plata"
            + "?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private DataBaseConnectionMySql() {
        connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar la base de datos: " + e.getMessage(), e);
        }
    }

    public static synchronized DataBaseConnectionMySql getInstance() {
        if (instance == null) {
            instance = new DataBaseConnectionMySql();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("Reconectando a la base de datos...");
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar conexión, reconectando: " + e.getMessage());
            connect();
        }
        System.out.println("Conectado a la base de datos mi_plata");
        return connection;
    }
}
