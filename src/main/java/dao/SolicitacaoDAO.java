package dao;

import db.DataBaseConfig;
import enums.StatusSolicitacao;
import model.Solicitacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    public void criarSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacoes (protocolo, tipo, status, prioridade, descricao, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, solicitacao.getSolicitacaoProtocolo());
            pstmt.setString(2, solicitacao.getTipoSolicitacao().name());
            pstmt.setString(3, solicitacao.getStatusSolicitacao().name());
            pstmt.setString(4, solicitacao.getPrioridade().name());
            pstmt.setString(5, solicitacao.getDescricao());
            pstmt.setInt(6, solicitacao.getUsuarioId());

            pstmt.executeUpdate();
        }
    }

    public List<Solicitacao> listarSolicitacoes() throws SQLException {
        String sql = "SELECT * FROM solicitacoes";
        List<Solicitacao> lista = new ArrayList<>();


        try (Connection conn = DataBaseConfig.getConnection();

             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Solicitacao s = new Solicitacao();
                s.setSolicitacaoId(rs.getInt("id"));
                s.setSolicitacaoProtocolo(rs.getString("protocolo"));
                s.setTipoSolicitacao(enums.TipoSolicitacao.valueOf(rs.getString("tipo")));
                s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
                s.setPrioridade(enums.Prioridade.valueOf(rs.getString("prioridade")));
                s.setDescricao(rs.getString("descricao"));
                s.setUsuarioId(rs.getInt("usuario_id"));
                s.setAtribuidoPara(rs.getString("atribuido_para"));
            }
        }

        return lista;
    }
    public void atribuirSolicitacao(int id, String nome) throws SQLException {
        String sql = "UPDATE solicitacoes SET atribuido_para = ? WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE status = ?";
        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setSolicitacaoId(rs.getInt("id"));
                    s.setSolicitacaoProtocolo(rs.getString("protocolo"));
                    s.setTipoSolicitacao(enums.TipoSolicitacao.valueOf(rs.getString("tipo")));
                    s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
                    s.setPrioridade(enums.Prioridade.valueOf(rs.getString("prioridade")));
                    s.setDescricao(rs.getString("descricao"));
                    s.setUsuarioId(rs.getInt("usuario_id"));
                    s.setAtribuidoPara(rs.getString("atribuido_para"));
                }
            }
        }

        return lista;
    }

    public Solicitacao buscarPorProtocolo(String protocolo) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE protocolo = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, protocolo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setSolicitacaoId(rs.getInt("id"));
                    s.setSolicitacaoProtocolo(rs.getString("protocolo"));
                    s.setTipoSolicitacao(enums.TipoSolicitacao.valueOf(rs.getString("tipo")));
                    s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
                    s.setPrioridade(enums.Prioridade.valueOf(rs.getString("prioridade")));
                    s.setDescricao(rs.getString("descricao"));
                    s.setUsuarioId(rs.getInt("usuario_id"));
                    s.setAtribuidoPara(rs.getString("atribuido_para"));
                    return s;
                }
            }
        }

        return null;
    }

    public Solicitacao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setSolicitacaoId(rs.getInt("id"));
                    s.setSolicitacaoProtocolo(rs.getString("protocolo"));
                    s.setTipoSolicitacao(enums.TipoSolicitacao.valueOf(rs.getString("tipo")));
                    s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
                    s.setPrioridade(enums.Prioridade.valueOf(rs.getString("prioridade")));
                    s.setDescricao(rs.getString("descricao"));
                    s.setUsuarioId(rs.getInt("usuario_id"));
                    return s;
                }
            }
        }

        return null;
    }

    public void atualizarStatus(int id, StatusSolicitacao novoStatus) throws SQLException {
        String sql = "UPDATE solicitacoes SET status = ? WHERE id = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus.name());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }
}