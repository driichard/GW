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
           return DriverManager
                    .getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
     }
  }




//public static void conexao (String... x) {

//}