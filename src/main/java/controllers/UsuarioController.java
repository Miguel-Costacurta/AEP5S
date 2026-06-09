package controllers;

import model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import repositories.IUsuarioRepository;

import java.util.List;

@RestController
public class UsuarioController {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioController(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USUARIO_GESTOR')")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> meuPerfil(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(usuarioLogado);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USUARIO_GESTOR')")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
