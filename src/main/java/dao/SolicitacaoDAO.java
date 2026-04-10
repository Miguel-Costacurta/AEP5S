package dao;

import db.DataBaseConfig;
import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import model.Solicitacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    public void criarSolicitacao(Solicitacao s) throws SQLException {
        String sql = "INSERT INTO solicitacoes " +
                "(protocolo, tipo, status, prioridade, descricao, localizacao, prazo, data_criacao, data_atualizacao, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getSolicitacaoProtocolo());
            pstmt.setString(2, s.getTipoSolicitacao().name());
            pstmt.setString(3, s.getStatusSolicitacao().name());
            pstmt.setString(4, s.getPrioridade().name());
            pstmt.setString(5, s.getDescricao());
            pstmt.setString(6, s.getLocalizacao());
            pstmt.setTimestamp(7, Timestamp.valueOf(s.getPrazo()));
            pstmt.setTimestamp(8, Timestamp.valueOf(s.getDataCriacao()));
            pstmt.setTimestamp(9, Timestamp.valueOf(s.getDataAtualizacao()));
            pstmt.setInt(10, s.getUsuarioId());
            pstmt.executeUpdate();
        }
    }

    public List<Solicitacao> listarSolicitacoes() throws SQLException {
        return listarComFiltro(null, null, null);
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) throws SQLException {
        return listarComFiltro(status, null, null);
    }

    public List<Solicitacao> listarPorPrioridade(Prioridade prioridade) throws SQLException {
        return listarComFiltro(null, prioridade, null);
    }

    public List<Solicitacao> listarPorTipo(TipoSolicitacao tipo) throws SQLException {
        return listarComFiltro(null, null, tipo);
    }

    public List<Solicitacao> listarPorLocalizacao(String bairro) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE LOWER(localizacao) LIKE LOWER(?) ORDER BY prioridade, data_criacao";
        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + bairro + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }


    private List<Solicitacao> listarComFiltro(StatusSolicitacao status, Prioridade prioridade, TipoSolicitacao tipo) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM solicitacoes WHERE 1=1");

        if (status != null)    sql.append(" AND status = '").append(status.name()).append("'");
        if (prioridade != null) sql.append(" AND prioridade = '").append(prioridade.name()).append("'");
        if (tipo != null)      sql.append(" AND tipo = '").append(tipo.name()).append("'");

        sql.append(" ORDER BY CASE prioridade WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END, data_criacao");

        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Solicitacao buscarPorProtocolo(String protocolo) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE protocolo = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, protocolo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
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
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void atualizarStatus(int id, StatusSolicitacao novoStatus) throws SQLException {
        String sql = "UPDATE solicitacoes SET status = ?, data_atualizacao = ? WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoStatus.name());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
    }

    private Solicitacao mapear(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao();
        s.setSolicitacaoId(rs.getInt("id"));
        s.setSolicitacaoProtocolo(rs.getString("protocolo"));
        s.setTipoSolicitacao(TipoSolicitacao.valueOf(rs.getString("tipo")));
        s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
        s.setPrioridade(Prioridade.valueOf(rs.getString("prioridade")));
        s.setDescricao(rs.getString("descricao"));
        s.setLocalizacao(rs.getString("localizacao"));
        s.setUsuarioId(rs.getInt("usuario_id"));

        Timestamp prazo = rs.getTimestamp("prazo");
        if (prazo != null) s.setPrazo(prazo.toLocalDateTime());

        Timestamp criacao = rs.getTimestamp("data_criacao");
        if (criacao != null) s.setDataCriacao(criacao.toLocalDateTime());

        return s;
    }
}