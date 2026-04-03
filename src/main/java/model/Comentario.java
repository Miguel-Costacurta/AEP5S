package model;

import java.time.LocalDateTime;

public class Comentario {
    private int comentarioId;
    private int solicitacaoId;
    private int usuarioId;
    private String texto;
    private LocalDateTime dataCriacao;

    public Comentario() {}

    public Comentario(int solicitacaoId, int usuarioId, String texto) {
        this.solicitacaoId = solicitacaoId;
        this.usuarioId = usuarioId;
        this.texto = texto;
        this.dataCriacao = LocalDateTime.now();
    }

    public int getComentarioId() { return comentarioId; }
    public void setComentarioId(int comentarioId) { this.comentarioId = comentarioId; }

    public int getSolicitacaoId() { return solicitacaoId; }
    public void setSolicitacaoId(int solicitacaoId) { this.solicitacaoId = solicitacaoId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}