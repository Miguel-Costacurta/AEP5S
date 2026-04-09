package dao;

import db.DataBaseConfig;
import enums.TipoUsuario;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO users (nome, email, senha, tipo_usuario) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsuarioNome());
            pstmt.setString(2, usuario.getUsuarioEmail());
            pstmt.setString(3, usuario.getUsuarioSenha());
            pstmt.setString(4, usuario.getTipoUsuario().name());

            pstmt.executeUpdate();
        }
    }

    public Usuario autenticarUsuario(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND senha = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuarioId(rs.getInt("id"));
                usuario.setUsuarioNome(rs.getString("nome"));
                usuario.setUsuarioEmail(rs.getString("email"));
                usuario.setUsuarioSenha(rs.getString("senha"));
                usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));
                return usuario;
            }
        }

        return null;
    }

    public boolean existeUsuarioPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        }
    }

    public void criarUsuariosPadrao() throws SQLException {
        if (!existeUsuarioPorEmail("gestor@sistema.com")) {
            Usuario gestor = new Usuario();
            gestor.setUsuarioNome("gestor");
            gestor.setUsuarioEmail("gestor@sistema.com");
            gestor.setUsuarioSenha("gestor123");
            gestor.setTipoUsuario(TipoUsuario.USUARIO_GESTOR);
            salvarUsuario(gestor);
        }

        if (!existeUsuarioPorEmail("atendente@sistema.com")) {
            Usuario atendente = new Usuario();
            atendente.setUsuarioNome("atendente");
            atendente.setUsuarioEmail("atendente@sistema.com");
            atendente.setUsuarioSenha("atendente123");
            atendente.setTipoUsuario(TipoUsuario.USUARIO_ATENDENTE);
            salvarUsuario(atendente);
        }
    }

    public List<Usuario> listarUsuario() throws SQLException {
        String sql = "SELECT * FROM users";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuarioId(rs.getInt("id"));
                usuario.setUsuarioNome(rs.getString("nome"));
                usuario.setUsuarioEmail(rs.getString("email"));
                usuario.setUsuarioSenha(rs.getString("senha"));
                usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }
}