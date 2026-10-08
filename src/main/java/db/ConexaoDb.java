package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDb {
    private static final String URL = "jdbc:postgresql://localhost:5432/david";
    private static final String USUARIO = "david";
    private static final String SENHA = "123456789";

public static void conexao (String... x) {
    try {
        Connection connection = DriverManager
                .getConnection(URL, USUARIO, SENHA);
        System.out.println("Recuperei e conexão");

        connection.close();
    } catch (SQLException e) {
        System.out.println(e);
    }
  }
}