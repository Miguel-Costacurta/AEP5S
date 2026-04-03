package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConfig {
    private static final String user = "root";
    private static final String password = "sa";
    private static final String url = "jdbc:h2:mem:testdb";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

    public static void initDatabase() {
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100)," +
                "email VARCHAR(100)," +
                "tipo VARCHAR(50)," +
                "senha VARCHAR(100))";
        String sqlSolicitacoes = "CREATE TABLE IF NOT EXISTS solicitacoes (" +
                "id INT AUTO_INCREMENT PRIMARY_KEY," +
                "tipo VARCHAR(100)," +
                "status VARCHAR(100))";
        String sqlComentarios = "CREATE TABLE IF NOT EXISTS comentarios (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "solicitacao_id INT," +
                "usuario_id INT," +
                "text VARCHAR(1000)," +
                "data_criacao TIMESTAMP )";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsers);
            stmt.execute(sqlSolicitacoes);
            stmt.execute(sqlComentarios);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
