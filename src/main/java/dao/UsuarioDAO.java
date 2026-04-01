package dao;

import db.DataBaseConfig;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO users " +
                "VALUES(?, ?, ?)";
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, usuario.getUsuarioId());
            pstmt.setString(2,usuario.getUsuarioNome());
            pstmt.setString(3,usuario.getUsuarioEmail());

            pstmt.executeUpdate();
        }
    }

    public void atualizarUsuario(Usuario usuario) throws SQLException{
        String sql = "UPDATE users SET nome = ? WHERE id = ?";
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, usuario.getUsuarioNome());
            pstmt.setString(2, usuario.getUsuarioId());
            }
        }

    public void deletarUsuario(String usuarioId) throws SQLException{
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1,usuarioId);
            pstmt.executeUpdate();
        }
    }

    public List<Usuario> listarUsuario() throws SQLException{
        String sql = "SELECT nome FROM users";
        List<Usuario> usuarios = new ArrayList<>();

        try(Connection conn = DataBaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                Usuario usuario = new Usuario();
                usuario.setUsuarioId(rs.getString("id"));
                usuario.setUsuarioNome(rs.getString("nome"));
                usuario.setUsuarioEmail(rs.getString("email"));

                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}
