package repositories;

import model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IHistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {
    List<HistoricoStatus> findBySolicitacaoIdOrderByDataAlteracaoAsc(Long solicitacaoId);
}
