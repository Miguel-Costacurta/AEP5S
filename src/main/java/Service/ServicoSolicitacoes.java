package Service;

import dao.ComentarioDAO;
import enums.StatusSolicitacao;
import enums.TipoUsuario;
import model.Comentario;
import model.Solicitacao;
import model.Usuario;

import java.sql.SQLException;
import java.sql.SQLOutput;

public class ServicoSolicitacoes {
    private ComentarioDAO comentarioDAO = new ComentarioDAO();

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

    public void mudarStatus(Solicitacao solicitacao, StatusSolicitacao novoStatus, Usuario responsavel, String observacao){
        if(!solicitacao.getStatusSolicitacao().podeMudar(novoStatus)){
            System.out.println("Não é possível alterar para esse status");
        }

        if(novoStatus == StatusSolicitacao.CANCELADA && !responsavel.getTipoUsuario().podeCancelar()){
            System.out.println("Apenas gestores podem cancelar uma solicitação");
        }

        if(!responsavel.getTipoUsuario().podeAtender()){
            System.out.println("Apenas atendentes ou gestores podem alterar o status");
        }



    }
}
