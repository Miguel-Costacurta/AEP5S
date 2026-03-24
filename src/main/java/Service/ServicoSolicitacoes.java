package Service;

import enums.StatusSolicitacao;
import model.Solicitacao;
import model.Usuario;

import java.sql.SQLOutput;

public class ServicoSolicitacoes {
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
