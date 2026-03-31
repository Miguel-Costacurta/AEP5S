package dao;

import db.DataBaseConfig;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
