package dao;

import db.DataBaseConfig;
import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import model.Solicitacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    public void criarSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = """
            INSERT INTO solicitacoes
            (protocolo, usuario_id, tipo, status, prioridade, descricao, data_criacao, data_atualizacao)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, solicitacao.getSolicitacaoProtocolo());
            pstmt.setInt(2, solicitacao.getUsuarioId());
            pstmt.setString(3, solicitacao.getTipoSolicitacao().name());
            pstmt.setString(4, solicitacao.getStatusSolicitacao().name());
            pstmt.setString(5, solicitacao.getPrioridade().name());
            pstmt.setString(6, solicitacao.getDescricao());
            pstmt.setDate(7, solicitacao.getDataCriacao());
            pstmt.setTimestamp(8, Timestamp.valueOf(solicitacao.getDataAtualizacao()));

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

    public List<Solicitacao> listarTodas() throws SQLException {
        String sql = "SELECT * FROM solicitacoes ORDER BY id";
        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearSolicitacao(rs));
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
                    return mapearSolicitacao(rs);
                }
            }
        }

        return null;
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE status = ? ORDER BY id";
        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearSolicitacao(rs));
                }
            }
        }

        return lista;
    }

    public List<Solicitacao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM solicitacoes WHERE usuario_id = ? ORDER BY id";
        List<Solicitacao> lista = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearSolicitacao(rs));
                }
            }
        }

        return lista;
    }

    private Solicitacao mapearSolicitacao(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao();

        s.setSolicitacaoId(rs.getInt("id"));
        s.setSolicitacaoProtocolo(rs.getString("protocolo"));
        s.setUsuarioId(rs.getInt("usuario_id"));
        s.setTipoSolicitacao(TipoSolicitacao.valueOf(rs.getString("tipo")));
        s.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status")));
        s.setPrioridade(Prioridade.valueOf(rs.getString("prioridade")));
        s.setDescricao(rs.getString("descricao"));

        Date dataCriacao = rs.getDate("data_criacao");
        if (dataCriacao != null) {
            s.setDataCriacao(dataCriacao);
        }

        Timestamp dataAtualizacao = rs.getTimestamp("data_atualizacao");
        if (dataAtualizacao != null) {
            s.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }

        return s;
    }
}