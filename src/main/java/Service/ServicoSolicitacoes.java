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

public class ServicoSolicitacoes {
    private final ComentarioDAO comentarioDAO = new ComentarioDAO();
    private final SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private final HistoricoStatusDAO historicoDAO = new HistoricoStatusDAO();

    public void mudarStatus(Solicitacao solicitacao, StatusSolicitacao novoStatus,
                            Usuario responsavel, String observacao) throws SQLException {


        if (observacao == null || observacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Observação é obrigatória para alterar o status.");
        }
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
        solicitacao.setStatusSolicitacao(novoStatus);

        solicitacaoDAO.atualizarStatus(solicitacao.getSolicitacaoId(), novoStatus);

        historicoDAO.registrar(new HistoricoStatus(
                solicitacao.getSolicitacaoId(), statusAnterior, novoStatus,
                observacao, responsavel.getUsuarioId(), responsavel.getUsuarioNome()
        ));
    }

    public void adicionarComentario(Solicitacao solicitacao, Usuario usuario, String texto) throws SQLException {
        if (usuario.isAnonimo()) {
            throw new IllegalStateException("Usuário anônimo não pode comentar.");
        }
        if (usuario.getTipoUsuario() == TipoUsuario.USUARIO_GESTOR) {
            throw new IllegalStateException("Gestor não pode comentar, apenas visualizar.");
        }
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio.");
        }
        comentarioDAO.registrar(new Comentario(
                solicitacao.getSolicitacaoId(), usuario.getUsuarioId(), texto
        ));
    }
}