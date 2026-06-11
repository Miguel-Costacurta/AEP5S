package service;

import repositories.IUsuarioRepository;
import enums.TipoUsuario;
import model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(IUsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    public Usuario cadastrarUsuarioLogado(Usuario usuario){
        if(usuarioRepository.existsByUsuarioEmail(usuario.getUsuarioEmail())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        usuario.setUsuarioSenha(passwordEncoder.encode(usuario.getUsuarioSenha()));

        if(usuario.getTipoUsuario() == null){
            usuario.setTipoUsuario(TipoUsuario.USUARIO_LOGADO);
        }
        return usuarioRepository.save(usuario);
    }

    public String login(String email, String senha) {
        UserDetails usuario = loadUserByUsername(email);

        if (!passwordEncoder.matches(senha, usuario.getPassword())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        return jwtService.gerarToken(usuario);
    }
}