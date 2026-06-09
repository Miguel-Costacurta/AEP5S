package model;

import enums.TipoSolicitacao;
import enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "tbl_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long usuarioId;
    @Column (name = "nome", nullable = false, length = 200)
    private String usuarioNome;
    @Column (name = "email", nullable = false, length = 255)
    private String usuarioEmail;
    @Column (name = "data_nascimento")
    private LocalDate usuarioDataNascimento;
    @Column (name = "senha", nullable = false, length = 255)
    private String usuarioSenha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private TipoUsuario tipoUsuario;


    public boolean isAnonimo(){
        return TipoUsuario.USUARIO_ANONIMO.equals(this.tipoUsuario);
    }

    public boolean podeCriar(TipoSolicitacao tipoSolicitacao){
        return this.tipoUsuario.podeCriar(tipoSolicitacao);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Cada TipoUsuario vira uma authority no padrão "ROLE_XXXX"
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return usuarioSenha;
    }

    @Override
    public String getUsername() {
        // O Spring Security usa o e-mail como identificador único
        return usuarioEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
