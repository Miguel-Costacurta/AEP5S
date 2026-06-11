package service;

import repositories.IComentarioRepository;
import repositories.IHistoricoStatusRepository;
import repositories.ISolicitacaoRepository;
import util.GeradorProtocolo;
import enums.StatusSolicitacao;
import enums.TipoUsuario;
import jakarta.transaction.Transactional;
import model.Comentario;
import model.HistoricoStatus;
import model.Solicitacao;
import model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoSolicitacoes {

    private final ISolicitacaoRepository solicitacaoRepository;
    private final IComentarioRepository comentarioRepository;
    private final IHistoricoStatusRepository historicoRepository;

    public ServicoSolicitacoes(ISolicitacaoRepository solicitacaoRepository,
                               IComentarioRepository comentarioRepository,
                               IHistoricoStatusRepository historicoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.comentarioRepository = comentarioRepository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional
    public Solicitacao criarSolicitacao(Solicitacao solicitacao, Usuario solicitante){
        if(!solicitante.podeCriar(solicitacao.getTipoSolicitacao())){
            throw new IllegalStateException("Usuário sem permissão para criar este tipo de solicitação");
        }

        String protocolo = new GeradorProtocolo(solicitante).getProtocolo();
        solicitacao.setSolicitacaoProtocolo(protocolo);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.PENDENTE);
        solicitacao.setUsuarioId(solicitante.getUsuarioId());

        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public Solicitacao criarSolicitacaoAnonima(Solicitacao solicitacao){
        java.time.LocalDateTime data = java.time.LocalDateTime.now();
        String protocolo = "OS" + data.getYear() + data.getMonthValue() + data.getDayOfMonth()
                + "ANON" + System.currentTimeMillis() % 100000;

        solicitacao.setSolicitacaoProtocolo(protocolo);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.PENDENTE);
        solicitacao.setUsuarioId(0L);

        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public void mudarStatus(Long solicitacaoId, StatusSolicitacao novoStatus,
                            Usuario responsavel, String observacao){

        Solicitacao solicitacao = buscarPorIdOuFalhar(solicitacaoId);

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
        solicitacaoRepository.save(solicitacao);

        historicoRepository.save(new HistoricoStatus(
                solicitacaoId, statusAnterior, novoStatus,
                observacao, responsavel.getUsuarioId(), responsavel.getUsuarioNome()
        ));
    }

    @Transactional
    public void adicionarComentario(Long solicitacaoId, Usuario usuario, String texto) {
        buscarPorIdOuFalhar(solicitacaoId);
        if (usuario.isAnonimo()) {
            throw new IllegalStateException("Usuário anônimo não pode comentar.");
        }
        if (usuario.getTipoUsuario() == TipoUsuario.USUARIO_GESTOR) {
            throw new IllegalStateException("Gestor não pode comentar, apenas visualizar.");
        }
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio.");
        }
        comentarioRepository.save(new Comentario(solicitacaoId, usuario.getUsuarioId(), texto));
    }

    public Solicitacao buscarPorProtocolo(String protocolo){
        return solicitacaoRepository.findBySolicitacaoProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada: "+ protocolo));
    }
    public List<Solicitacao> listarTodas(){
        return solicitacaoRepository.findAll();
    }

    public List<Solicitacao> listarAtrasadas() {
        return solicitacaoRepository.findAtrasadas();
    }

    public List<Comentario> listarComentarios(Long solicitacaoId) {
        buscarPorIdOuFalhar(solicitacaoId);
        return comentarioRepository.findBySolicitacaoIdOrderByDataCriacaoAsc(solicitacaoId);
    }

    public List<HistoricoStatus> listarHistorico(Long solicitacaoId) {
        buscarPorIdOuFalhar(solicitacaoId);
        return historicoRepository.findBySolicitacaoIdOrderByDataAlteracaoAsc(solicitacaoId);
    }

    private Solicitacao buscarPorIdOuFalhar(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada: " + id));
    }
}