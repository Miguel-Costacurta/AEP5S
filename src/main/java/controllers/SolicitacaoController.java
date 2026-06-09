package controllers;

import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import lombok.Getter;
import model.Comentario;
import model.HistoricoStatus;
import model.Solicitacao;
import model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import repositories.ISolicitacaoRepository;
import service.ServicoSolicitacoes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    private final ServicoSolicitacoes servicoSolicitacoes;
    private final ISolicitacaoRepository solicitacaoRepository;

    public SolicitacaoController(ServicoSolicitacoes servicoSolicitacoes,
                                 ISolicitacaoRepository solicitacaoRepository) {
        this.servicoSolicitacoes = servicoSolicitacoes;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USUARIO_LOGADO','USUARIO_GESTOR')")
    public ResponseEntity<Solicitacao> criar(@RequestBody Solicitacao solicitacao,
                                             @AuthenticationPrincipal Usuario usuarioLogado){
        Solicitacao criada = servicoSolicitacoes.criarSolicitacao(solicitacao, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USUARIO_ATENDENTE', 'USUARIO_GESTOR')")
    public ResponseEntity<List<Solicitacao>> listarTodas(){
        return ResponseEntity.ok(servicoSolicitacoes.listarTodas());
    }

    @GetMapping("/atrasadas")
    @PreAuthorize("hasRole('USUARIO_GESTOR')")
    public ResponseEntity<List<Solicitacao>> atrasadas(){
        return ResponseEntity.ok(servicoSolicitacoes.listarAtrasadas());
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<Solicitacao> buscarPorProtocolo(@PathVariable String protocolo) {
        return ResponseEntity.ok(servicoSolicitacoes.buscarPorProtocolo(protocolo));
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasAnyRole('USUARIO_ATENDENTE', 'USUARIO_GESTOR')")
    public ResponseEntity<List<Solicitacao>> filtrar(
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) TipoSolicitacao tipo,
            @RequestParam(required = false) String localizacao) {

        List<Solicitacao> resultado;

        if (localizacao != null) {
            resultado = solicitacaoRepository.findByLocalizacaoContainignIgnoreCase(localizacao);
        } else if (status != null && prioridade != null) {
            resultado = solicitacaoRepository.findByStatusSolicitacaoAndPrioridade(status, prioridade);
        } else if (status != null) {
            resultado = solicitacaoRepository.findByStatusSolicitacao(status);
        } else if (prioridade != null) {
            resultado = solicitacaoRepository.findByPrioridadeSolicitacao(prioridade);
        } else if (tipo != null) {
            resultado = solicitacaoRepository.findByTipoSolicitacao(tipo);
        } else {
            resultado = servicoSolicitacoes.listarTodas();
        }

        return ResponseEntity.ok(resultado);
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('USUARIO_ATENDENTE', 'USUARIO_GESTOR')")
    public ResponseEntity<Void> mudarStatus(@PathVariable Long id,
                                            @RequestBody Map<String, String> body,
                                            @AuthenticationPrincipal Usuario usuarioLogado) {
        StatusSolicitacao novoStatus = StatusSolicitacao.valueOf(body.get("novoStatus"));
        String observacao = body.get("observacao");
        servicoSolicitacoes.mudarStatus(id, novoStatus, usuarioLogado, observacao);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/comentarios")
    @PreAuthorize("hasAnyRole('USUARIO_LOGADO', 'USUARIO_ATENDENTE')")
    public ResponseEntity<Void> comentar(@PathVariable Long id,
                                         @RequestBody Map<String, String> body,
                                         @AuthenticationPrincipal Usuario usuarioLogado) {
        servicoSolicitacoes.adicionarComentario(id, usuarioLogado, body.get("texto"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/{id}/comentarios")
    @PreAuthorize("hasAnyRole('USUARIO_LOGADO', 'USUARIO_ATENDENTE', 'USUARIO_GESTOR')")
    public ResponseEntity<List<Comentario>> listarComentarios(@PathVariable Long id) {
        return ResponseEntity.ok(servicoSolicitacoes.listarComentarios(id));
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasAnyRole('USUARIO_ATENDENTE', 'USUARIO_GESTOR')")
    public ResponseEntity<List<HistoricoStatus>> listarHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(servicoSolicitacoes.listarHistorico(id));
    }
}
