package repositories;

import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ISolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByStatusSolicitacao(StatusSolicitacao status);
    List<Solicitacao> findByPrioridadeSolicitacao(Prioridade prioridade);
    List<Solicitacao> findByTipoSolicitacao(TipoSolicitacao tipoSolicitacao);
    List<Solicitacao> findByUsuarioId(Long usuarioId);

    Optional<Solicitacao> findBySolicitacaoProtocolo(String protocolo);

    List<Solicitacao> findByLocalizacaoContainignIgnoreCase(String localizacao);

    List<Solicitacao> findByStatusSolicitacaoAndPrioridade(StatusSolicitacao statusSolicitacao, Prioridade prioridade);

    @Query("""
           SELECT s FROM Solicitacao s
           WHERE s.prazo < CURRENT_TIMESTAMP
             AND s.statusSolicitacao NOT IN ( enums.StatusSolicitacao.CONCLUIDA,
                                              enums.StatuisSolicitacao.CANCELADA)
           ORDER BY s.prazo ASC;
           """)
    List<Solicitacao> findAtrasadas();
}
