package Service;

import dao.ComentarioDAO;
import dao.HistoricoStatusDAO;
import dao.SolicitacaoDAO;
import enums.StatusSolicitacao;
import enums.TipoUsuario;
import model.Comentario;
import model.HistoricoStatus;
import model.Solicitacao;
import model.Usuario;

import java.sql.SQLException;
import java.sql.SQLOutput;

public class ServicoSolicitacoes {
    private ComentarioDAO comentarioDAO = new ComentarioDAO();
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private HistoricoStatusDAO historicoDAO = new HistoricoStatusDAO();

    public void adicionarComentario(Solicitacao solicitacao, Usuario usuario, String texto) throws SQLException {
        if (usuario.isAnonimo()) {
            throw new IllegalStateException("Usuário anônimo não pode comentar.");
        }

        if (usuario.getTipoUsuario() == TipoUsuario.USUARIO_GESTOR) {
            throw new IllegalStateException("Gestor não pode comentar, apenas visualizar.");
        }

        Comentario comentario = new Comentario(
                solicitacao.getSolicitacaoId(),
                usuario.getUsuarioId(),
                texto
        );
        comentarioDAO.registrar(comentario);
    }

    public void mudarStatus(Solicitacao solicitacao, StatusSolicitacao novoStatus,
                            Usuario responsavel, String observacao) throws SQLException {

        if (!solicitacao.getStatusSolicitacao().podeMudar(novoStatus)) {
            throw new IllegalStateException("Transição inválida: "
                    + solicitacao.getStatusSolicitacao() + " → " + novoStatus);
        }
        if (novoStatus == StatusSolicitacao.CANCELADA && !responsavel.getTipoUsuario().podeCancelar()) {
            throw new IllegalStateException("Apenas gestores podem cancelar.");
        }
        if (!responsavel.getTipoUsuario().podeAtender()) {
            throw new IllegalStateException("Apenas atendentes ou gestores podem alterar o status.");
        }

        StatusSolicitacao statusAnterior = solicitacao.getStatusSolicitacao();
        solicitacao.setStatusSolicitacao(novoStatus); // já atualiza dataAtualizacao

        solicitacaoDAO.atualizarStatus(solicitacao.getSolicitacaoId(), solicitacao.getStatusSolicitacao());

        HistoricoStatus historico = new HistoricoStatus(
                solicitacao.getSolicitacaoId(), statusAnterior, novoStatus,
                observacao, responsavel.getUsuarioId()
        );
        historicoDAO.registrar(historico);
    }
}
