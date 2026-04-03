package model;

import enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class HistoricoStatus {
    private int historicoId;
    private int solicitacaoId;
    private StatusSolicitacao statusAnterior;
    private StatusSolicitacao statusAtual;
    private String observacao;
    private LocalDateTime dataAlteracao;

    private int usuarioId;

    public HistoricoStatus(){}

    public HistoricoStatus(int solicitacaoId, StatusSolicitacao statusAnterior, StatusSolicitacao statusAtual,
                            String observacao, int usuarioId){

        this.solicitacaoId = solicitacaoId;
        this.statusAnterior = statusAnterior;
        this.statusAtual = statusAtual;
        this.observacao = observacao;
        this.usuarioId = usuarioId;
        this.dataAlteracao = LocalDateTime.now();
    }

    public int getHistoricoId() {
        return historicoId;
    }
    public void setHistoricoId(int historicoId) {
        this.historicoId = historicoId;
    }

    public int getSolicitacaoId() {
        return solicitacaoId;
    }
    public void setSolicitacaoId(int solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public StatusSolicitacao getStatusAnterior() {
        return statusAnterior;
    }
    public void setStatusAnterior(StatusSolicitacao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusSolicitacao getStatusAtual() {
        return statusAtual;
    }
    public void setStatusAtual(StatusSolicitacao statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }
    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
