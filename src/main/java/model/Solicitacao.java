package model;

import dao.SolicitacaoDAO;
import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;

public class Solicitacao {
    private int solicitacaoId;
    private String solicitacaoProtocolo;
    private TipoSolicitacao tipoSolicitacao;
    private StatusSolicitacao statusSolicitacao;
    private Prioridade prioridade;
    private String descricao;
    private Date dataCriacao;
    private LocalDateTime dataAtualizacao;

    private int usuarioId;

    private SolicitacaoDAO solicitacaoDAO;

    public Solicitacao(){}

    public Solicitacao(String protocolo, TipoSolicitacao tipoSolicitacao, Prioridade prioridade, String descricao
                        ,int usuarioId){
        this.solicitacaoProtocolo = protocolo;
        this.tipoSolicitacao = tipoSolicitacao;
        this.statusSolicitacao = StatusSolicitacao.PENDENTE;
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.dataCriacao = (Date) Date.from(Instant.now());
        this.dataAtualizacao = LocalDateTime.now();
    }

    public int getSolicitacaoId() {
        return solicitacaoId;
    }
    public void setSolicitacaoId(int solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public String getSolicitacaoProtocolo() {
        return solicitacaoProtocolo;
    }
    public void setSolicitacaoProtocolo(String solicitacaoProtocolo) {
        this.solicitacaoProtocolo = solicitacaoProtocolo;
    }

    public TipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }
    public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public StatusSolicitacao getStatusSolicitacao() {
        return statusSolicitacao;
    }
    public void setStatusSolicitacao(StatusSolicitacao statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void criarSolicitacaoBanco(Solicitacao solicitacao) throws SQLException {
        solicitacaoDAO.criarSolicitacao(solicitacao);
    }
}
