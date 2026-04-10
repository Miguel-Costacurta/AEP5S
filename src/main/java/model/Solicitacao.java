package model;

import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;

import java.sql.Date;
import java.time.LocalDateTime;

public class Solicitacao {
    private int solicitacaoId;
    private String solicitacaoProtocolo;
    private TipoSolicitacao tipoSolicitacao;
    private StatusSolicitacao statusSolicitacao;
    private Prioridade prioridade;
    private String descricao;
    private String localizacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime prazo;
    private int usuarioId;

    public Solicitacao() {}

    public Solicitacao(String protocolo, TipoSolicitacao tipoSolicitacao, Prioridade prioridade,
                       String descricao, String localizacao, int usuarioId) {
        this.solicitacaoProtocolo = protocolo;
        this.tipoSolicitacao = tipoSolicitacao;
        this.statusSolicitacao = StatusSolicitacao.PENDENTE;
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.usuarioId = usuarioId;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.prazo = LocalDateTime.now().plusDays(prioridade.getDiasSLA());
    }

    public boolean estaAtrasada() {
        if (statusSolicitacao == StatusSolicitacao.CONCLUIDA ||
                statusSolicitacao == StatusSolicitacao.CANCELADA) {
            return false;
        }
        return LocalDateTime.now().isAfter(prazo);
    }

    public int getSolicitacaoId() { return solicitacaoId; }
    public void setSolicitacaoId(int solicitacaoId) { this.solicitacaoId = solicitacaoId; }

    public String getSolicitacaoProtocolo() { return solicitacaoProtocolo; }
    public void setSolicitacaoProtocolo(String p) { this.solicitacaoProtocolo = p; }

    public TipoSolicitacao getTipoSolicitacao() { return tipoSolicitacao; }
    public void setTipoSolicitacao(TipoSolicitacao t) { this.tipoSolicitacao = t; }

    public StatusSolicitacao getStatusSolicitacao() { return statusSolicitacao; }
    public void setStatusSolicitacao(StatusSolicitacao s) {
        this.statusSolicitacao = s;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public LocalDateTime getPrazo() { return prazo; }
    public void setPrazo(LocalDateTime prazo) { this.prazo = prazo; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
}