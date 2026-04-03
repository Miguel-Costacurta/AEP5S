package dao;

import db.DataBaseConfig;
import enums.StatusSolicitacao;
import model.Solicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SolicitacaoDAO {
    private String statusSolicitacao;
    public void criarSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacoes " +
                "VALUES(?,?,?,?,?)";
        switch(solicitacao.getStatusSolicitacao()){
            case StatusSolicitacao.PENDENTE -> statusSolicitacao = "Pendente";
            case StatusSolicitacao.CANCELADA -> statusSolicitacao = "Cancelada";
            case StatusSolicitacao.EM_ATENDIMENTO -> statusSolicitacao = "Em_atendimento";
            case StatusSolicitacao.CONCLUIDA -> statusSolicitacao = "Concluida";
            case StatusSolicitacao.AGUARDANDO_RESPOSTA -> statusSolicitacao = "Aguardando_resposta";
        }
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, solicitacao.getSolicitacaoId());
            pstmt.setString(2,solicitacao.getSolicitacaoProtocolo());
            pstmt.setInt(3,solicitacao.getUsuarioId());
            pstmt.setString(4,statusSolicitacao);
            pstmt.setDate(5,solicitacao.getDataCriacao());

            pstmt.executeUpdate();
        }
    }

    public void atualizarStatus(Solicitacao s) throws SQLException {
        String sql = "UPDATE solicitacoes SET status = ?, data_atualizacao = ? WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getStatusSolicitacao().name());
            pstmt.setTimestamp(2, Timestamp.valueOf(s.getDataAtualizacao()));
            pstmt.setInt(3, s.getSolicitacaoId());
            pstmt.executeUpdate();
        }
    }
}
