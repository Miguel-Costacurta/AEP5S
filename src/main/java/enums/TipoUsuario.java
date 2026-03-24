package enums;

import java.util.Set;

public enum TipoUsuario {

    USUARIO_LOGADO(Set.of(TipoSolicitacao.SOLICITACAO_ASSEDIO, TipoSolicitacao.SOLICITACAO_BURACAO,TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA,
                          TipoSolicitacao.SOLICITACAO_PODA, TipoSolicitacao.SOLICITACAO_REFORMA, TipoSolicitacao.SOLICITACAO_ILUMINACAO)),
    USUARIO_ANONIMO(Set.of(TipoSolicitacao.SOLICITACAO_PODA, TipoSolicitacao.SOLICITACAO_BURACAO, TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA,
                           TipoSolicitacao.SOLICITACAO_ILUMINACAO, TipoSolicitacao.SOLICITACAO_REFORMA)),
    USUARIO_ATENDENTE

    private Set<TipoSolicitacao> solicitacaoPermitidas;

    TipoUsuario(Set<TipoSolicitacao> solicitacaoPermitidas){
        this.solicitacaoPermitidas = solicitacaoPermitidas;
    }

    public boolean podeCriar(TipoSolicitacao tipo){
        return solicitacaoPermitidas.contains(tipo);
    }

}
