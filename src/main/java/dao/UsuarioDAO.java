package dao;

import db.DataBaseConfig;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private String tipoUsuario;

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO users (nome, email, tipo, senha) VALUES (?, ?, ?, ?)";

        switch (usuario.getTipoUsuario()){
            case USUARIO_ANONIMO -> tipoUsuario = "Anonimo";
            case USUARIO_LOGADO -> tipoUsuario = "Logado";
            case USUARIO_BAIXA_RENDA -> tipoUsuario = "Baixa Renda";
            case USUARIO_VULNERABILIDADE -> tipoUsuario = "Vulnerabilidade";
            case USUARIO_SERVIDOR -> tipoUsuario = "Servidor";
            case USUARIO_ATENDENTE -> tipoUsuario = "Atendente";
            case USUARIO_GESTOR -> tipoUsuario = "Gestor";
        }

        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, usuario.getUsuarioNome());
            pstmt.setString(2, usuario.getUsuarioEmail());
            pstmt.setString(3, tipoUsuario);
            pstmt.setString(4, usuario.getUsuarioSenha());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setUsuarioId(rs.getInt(1));
            }
        }
    }

    public void atualizarNome(Usuario usuario) throws SQLException{
        String sql = "UPDATE users SET nome = ? WHERE id = ?";
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, usuario.getUsuarioNome());
            pstmt.setInt(2, usuario.getUsuarioId());
            pstmt.executeUpdate();
        }
    }

    public void atualizarSenha(Usuario usuario) throws SQLException{
        String sql = "UPDATE users SET senha = ? WHERE id = ?";

        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, usuario.getUsuarioSenha());
            pstmt.setInt(2, usuario.getUsuarioId());
            pstmt.executeUpdate();
        }
    }

    public void deletarUsuario(String usuarioId) throws SQLException{
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, usuarioId);
            pstmt.executeUpdate();
        }
    }

    public List<Usuario> listarUsuario() throws SQLException{
        String sql = "SELECT id, nome, email FROM users";
        List<Usuario> usuarios = new ArrayList<>();

        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                Usuario usuario = new Usuario();
                usuario.setUsuarioId(rs.getInt("id"));
                usuario.setUsuarioNome(rs.getString("nome"));
                usuario.setUsuarioEmail(rs.getString("email"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}