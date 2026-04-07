package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConfig {
    private static final String user = "root";
    private static final String password = "sa";
    private static final String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

    public static void initDatabase() {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nome VARCHAR(100),
                email VARCHAR(100),
                tipo VARCHAR(50),
                senha VARCHAR(100)
            )
        """;

        String sqlSolicitacoes = """
            CREATE TABLE IF NOT EXISTS solicitacoes (
                id INT PRIMARY KEY AUTO_INCREMENT,
                protocolo VARCHAR(100) NOT NULL,
                usuario_id INT,
                tipo VARCHAR(100),
                status VARCHAR(100),
                prioridade VARCHAR(50),
                descricao VARCHAR(1000),
                data_criacao DATE,
                data_atualizacao TIMESTAMP,
                FOREIGN KEY (usuario_id) REFERENCES users(id)
            )
        """;

        String sqlComentarios = """
            CREATE TABLE IF NOT EXISTS comentarios (
                id INT PRIMARY KEY AUTO_INCREMENT,
                solicitacao_id INT,
                usuario_id INT,
                texto VARCHAR(1000),
                data_criacao TIMESTAMP,
                FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
                FOREIGN KEY (usuario_id) REFERENCES users(id)
            )
        """;

        String sqlHistoricoStatus = """
            CREATE TABLE IF NOT EXISTS historico_status (
                id INT PRIMARY KEY AUTO_INCREMENT,
                solicitacao_id INT,
                status_anterior VARCHAR(100),
                status_novo VARCHAR(100),
                observacao VARCHAR(1000),
                data_alteracao TIMESTAMP,
                usuario_id INT,
                FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
                FOREIGN KEY (usuario_id) REFERENCES users(id)
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsers);
            stmt.execute(sqlSolicitacoes);
            stmt.execute(sqlComentarios);
            stmt.execute(sqlHistoricoStatus);
            stmt.execute("INSERT INTO users (id, nome, email, tipo, senha) " +
                    "SELECT 0, 'Anonimo', '', 'Anonimo', '' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 0)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
