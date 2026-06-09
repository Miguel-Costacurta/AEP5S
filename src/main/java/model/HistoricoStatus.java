package model;

import enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_historico_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long historicoId;

    @Column(name = "solicitacao_id", nullable = false)
    private Long solicitacaoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", nullable = false, length = 50)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 50)
    private StatusSolicitacao statusAtual;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @CreationTimestamp
    @Column(name = "data_alteracao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nome_responsavel", length = 100)
    private String nomeResponsavel;



    public HistoricoStatus(Long solicitacaoId, StatusSolicitacao statusAnterior,
                           StatusSolicitacao statusAtual, String observacao,
                           Long usuarioId, String nomeResponsavel) {
        this.solicitacaoId = solicitacaoId;
        this.statusAnterior = statusAnterior;
        this.statusAtual = statusAtual;
        this.observacao = observacao;
        this.usuarioId = usuarioId;
        this.nomeResponsavel = nomeResponsavel;
    }
}
