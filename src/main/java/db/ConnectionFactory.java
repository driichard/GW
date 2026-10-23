package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            DriverManager.setLoginTimeout(5);
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/david",
                    "david",
                    "123456789"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
