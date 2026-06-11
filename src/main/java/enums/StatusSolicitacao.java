package enums;

import java.util.EnumSet;
import java.util.Set;

public enum StatusSolicitacao {
    PENDENTE,
    EM_ATENDIMENTO,
    AGUARDANDO_RESPOSTA,
    CONCLUIDA,
    CANCELADA;

    private Set<StatusSolicitacao> fluxoPermitido;

    static {
        PENDENTE.fluxoPermitido           = EnumSet.of(EM_ATENDIMENTO, CANCELADA);
        EM_ATENDIMENTO.fluxoPermitido     = EnumSet.of(AGUARDANDO_RESPOSTA, CONCLUIDA, CANCELADA);
        AGUARDANDO_RESPOSTA.fluxoPermitido = EnumSet.of(EM_ATENDIMENTO, CONCLUIDA, CANCELADA);
        CONCLUIDA.fluxoPermitido          = EnumSet.noneOf(StatusSolicitacao.class);
        CANCELADA.fluxoPermitido          = EnumSet.noneOf(StatusSolicitacao.class);
    }


    public boolean podeMudar(StatusSolicitacao novo){
        return fluxoPermitido.contains(novo);
    }
}
