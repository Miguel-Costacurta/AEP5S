package model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_comentario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long comentarioId;

    // FK para solicitação
    @Column(name = "solicitacao_id", nullable = false)
    private Long solicitacaoId;

    // FK para usuário
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "texto", nullable = false, length = 1000)
    private String texto;

    // @CreationTimestamp: o Hibernate preenche automaticamente na inserção,
    // sem precisar setar manualmente no construtor
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    public Comentario(Long solicitacaoId, Long usuarioId, String texto) {
        this.solicitacaoId = solicitacaoId;
        this.usuarioId = usuarioId;
        this.texto = texto;
    }
}