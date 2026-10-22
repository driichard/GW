package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/david";
    private static final String USUARIO = "david";
    private static final String SENHA = "123456789";

    public Connection recuperarConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            DriverManager.setLoginTimeout(5);
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
     }
  }
