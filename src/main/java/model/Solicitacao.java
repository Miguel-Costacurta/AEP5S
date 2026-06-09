package model;

import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long solicitacaoId;

    @Column(name = "protocolo", length = 100)
    private String solicitacaoProtocolo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 100)
    private TipoSolicitacao tipoSolicitacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StatusSolicitacao statusSolicitacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false, length = 50)
    private Prioridade prioridade;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "localizacao", length = 255)
    private String localizacao;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "prazo")
    private LocalDateTime prazo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    public Solicitacao(String protocolo, TipoSolicitacao tipoSolicitacao, Prioridade prioridade,
                       String descricao, String localizacao, Long usuarioId) {
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

    public void setStatusSolicitacao(StatusSolicitacao s) {
        this.statusSolicitacao = s;
        this.dataAtualizacao = LocalDateTime.now();
    }

}