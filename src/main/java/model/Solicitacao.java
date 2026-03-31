package model;

import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;

import java.time.LocalDateTime;
import java.util.Locale;

public class Solicitacao {
    private int solicitacaoId;
    private String solicitacaoProtocolo;
    private int tipoSolicitacao;
    private StatusSolicitacao statusSolicitacao;
    private String prioridade;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    private int usuarioId;

    public Solicitacao(){}

    public Solicitacao(String protocolo, int tipoSolicitacao, String prioridade, String descricao
                        ,int usuarioId){
        this.solicitacaoProtocolo = protocolo;
        this.tipoSolicitacao = tipoSolicitacao;
        this.statusSolicitacao = StatusSolicitacao.PENDENTE;
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.dataCriacao = LocalDateTime.now();
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

    public int getTipoSolicitacao() {
        return tipoSolicitacao;
    }
    public void setTipoSolicitacao(int tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public StatusSolicitacao getStatusSolicitacao() {
        return statusSolicitacao;
    }
    public void setStatusSolicitacao(StatusSolicitacao statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
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
}
