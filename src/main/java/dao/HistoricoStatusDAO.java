package dao;

import db.DataBaseConfig;
import enums.StatusSolicitacao;
import model.HistoricoStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoStatusDAO {

    public void registrar(HistoricoStatus h) throws SQLException {
        String sql = """
            INSERT INTO historico_status 
            (solicitacao_id, status_anterior, status_novo, observacao, data_alteracao, usuario_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, h.getSolicitacaoId());
            pstmt.setString(2, h.getStatusAnterior().name());
            pstmt.setString(3, h.getStatusAtual().name());
            pstmt.setString(4, h.getObservacao());
            pstmt.setTimestamp(5, Timestamp.valueOf(h.getDataAlteracao()));
            pstmt.setInt(6, h.getUsuarioId());
            pstmt.executeUpdate();
        }
    }

    public List<HistoricoStatus> buscarPorSolicitacao(int solicitacaoId) throws SQLException {
        String sql = "SELECT hs.*, u.nome " +
                "FROM historico_status hs " +
                "JOIN users u ON u.id = hs.usuario_id " +
                "WHERE hs.solicitacao_id = ? " +
                "ORDER BY hs.data_alteracao ";
        List<HistoricoStatus> lista = new ArrayList<>();
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, solicitacaoId);
            try (ResultSet rs = pstmt.executeQuery();
                ) {
                while (rs.next()) {
                    HistoricoStatus h = new HistoricoStatus();
                    h.setHistoricoId(rs.getInt("id"));
                    h.setSolicitacaoId(rs.getInt("solicitacao_id"));
                    h.setStatusAnterior(StatusSolicitacao.valueOf(rs.getString("status_anterior")));
                    h.setStatusAtual(StatusSolicitacao.valueOf(rs.getString("status_novo")));
                    h.setObservacao(rs.getString("observacao"));
                    h.setDataAlteracao(rs.getTimestamp("data_alteracao").toLocalDateTime());
                    h.setUsuarioId(rs.getInt("usuario_id"));
                    h.setNomeResponsavel(rs.getString("nome"));
                    lista.add(h);
                }
            }
        }
        return lista;
    }
}