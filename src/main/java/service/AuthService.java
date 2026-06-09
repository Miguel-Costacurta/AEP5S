package service;

import repositories.IUsuarioRepository;
import enums.TipoUsuario;
import model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AuthService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(IUsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );

        UserDetails usuario = loadUserByUsername(email);
        return jwtService.gerarToken(usuario);
    }
}