package Service;

import dao.UsuarioDAO;
import enums.TipoUsuario;
import model.Usuario;

import java.sql.SQLException;

public class AuthService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrarUsuarioLogado(String nome, String email, String senha) throws SQLException {
        cadastrarUsuario(nome, email, senha, TipoUsuario.USUARIO_LOGADO);
    }

    public void cadastrarAtendente(String nome, String email, String senha) throws SQLException {
        cadastrarUsuario(nome, email, senha, TipoUsuario.USUARIO_ATENDENTE);
    }

    public void cadastrarGestor(String nome, String email, String senha) throws SQLException {
        cadastrarUsuario(nome, email, senha, TipoUsuario.USUARIO_GESTOR);
    }

    private void cadastrarUsuario(String nome, String email, String senha, TipoUsuario tipoUsuario) throws SQLException {
        if (usuarioDAO.existeUsuarioPorEmail(email)) {
            System.out.println("Já existe usuário com esse email.");
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setUsuarioNome(nome);
        usuario.setUsuarioEmail(email);
        usuario.setUsuarioSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);

        usuarioDAO.salvarUsuario(usuario);

        System.out.println("Usuário cadastrado com sucesso.");
    }

    public Usuario login(String email, String senha) throws SQLException {
        Usuario usuario = usuarioDAO.autenticarUsuario(email, senha);

        if (usuario == null) {
            System.out.println("Email ou senha inválidos.");
            return null;
        }

        return usuario;
    }
}