package enums;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public enum TipoUsuario {

    USUARIO_LOGADO(Set.of(TipoSolicitacao.DENUNCIA_ASSEDIO, TipoSolicitacao.DENUNCIA_BURACO,TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA,
                          TipoSolicitacao.SOLICITACAO_PODA, TipoSolicitacao.SOLICITACAO_REFORMA, TipoSolicitacao.SOLICITACAO_ILUMINACAO)),
    USUARIO_ANONIMO(Set.of(TipoSolicitacao.SOLICITACAO_PODA, TipoSolicitacao.DENUNCIA_BURACO, TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA,
                           TipoSolicitacao.SOLICITACAO_ILUMINACAO, TipoSolicitacao.SOLICITACAO_REFORMA)),
    USUARIO_ATENDENTE(Collections.emptySet()),
    USUARIO_GESTOR(Set.of(
            TipoSolicitacao.DENUNCIA_ASSEDIO,
            TipoSolicitacao.DENUNCIA_BURACO,
            TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA,
            TipoSolicitacao.SOLICITACAO_PODA,
            TipoSolicitacao.SOLICITACAO_REFORMA,
            TipoSolicitacao.SOLICITACAO_ILUMINACAO
    ));

    private Set<TipoSolicitacao> solicitacaoPermitidas;

    TipoUsuario(Set<TipoSolicitacao> solicitacaoPermitidas){
        this.solicitacaoPermitidas = solicitacaoPermitidas;
    }

    public boolean podeCriar(TipoSolicitacao tipo){
        return solicitacaoPermitidas.contains(tipo);
    }

    public boolean podeAtender() {
        return this == USUARIO_ATENDENTE || this == USUARIO_GESTOR;
    }

    public boolean podeCancelar() {
        return this == USUARIO_GESTOR;
    }

}
