package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConfig {

    private static final String URL = "jdbc:h2:./data/centraldb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "senha VARCHAR(100) NOT NULL, " +
                    "tipo_usuario VARCHAR(30) NOT NULL" +
                    ")";

            String sqlSolicitacoes = "CREATE TABLE IF NOT EXISTS solicitacoes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "protocolo VARCHAR(100), " +
                    "tipo VARCHAR(100), " +
                    "status VARCHAR(100), " +
                    "prioridade VARCHAR(50), " +
                    "descricao VARCHAR(255), " +
                    "usuario_id INT" +
                    ")";

            stmt.execute(sqlUsers);
            stmt.execute(sqlSolicitacoes);

            System.out.println("Banco inicializado com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}