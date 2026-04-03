package dao;

import db.DataBaseConfig;
import model.Comentario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    public void registrar(Comentario c) throws SQLException {
        String sql = """
            INSERT INTO comentarios (solicitacao_id, usuario_id, texto, data_criacao)
            VALUES (?, ?, ?, ?)
        """;
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getSolicitacaoId());
            pstmt.setInt(2, c.getUsuarioId());
            pstmt.setString(3, c.getTexto());
            pstmt.setTimestamp(4, Timestamp.valueOf(c.getDataCriacao()));
            pstmt.executeUpdate();
        }
    }

    public List<Comentario> buscarPorSolicitacao(int solicitacaoId) throws SQLException {
        String sql = "SELECT * FROM comentarios WHERE solicitacao_id = ? ORDER BY data_criacao ASC";
        List<Comentario> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, solicitacaoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comentario c = new Comentario();
                    c.setComentarioId(rs.getInt("id"));
                    c.setSolicitacaoId(rs.getInt("solicitacao_id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setTexto(rs.getString("texto"));
                    c.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                    lista.add(c);
                }
            }
        }
        return lista;
    }
}
