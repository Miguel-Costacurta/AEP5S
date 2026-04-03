package model;

import enums.TipoSolicitacao;
import enums.TipoUsuario;

import java.sql.Date;
import java.time.LocalDateTime;

public class Usuario {
    private int usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private LocalDateTime usuarioDataNascimento;
    private String usuarioSenha;

    private TipoUsuario tipoUsuario;

    public Usuario(){}

    public Usuario(TipoUsuario tipo){
        this.tipoUsuario = tipo;
    }

    public Usuario(String nome, TipoUsuario tipo, String usuarioSenha, String usuarioEmail, LocalDateTime usuarioDataNascimento ){
        this.usuarioNome = nome;
        this.tipoUsuario = tipo;
        this.usuarioEmail = usuarioEmail;
        this.usuarioSenha = usuarioSenha;
        this.usuarioDataNascimento = usuarioDataNascimento;
    }

    public boolean isAnonimo(){
        return TipoUsuario.USUARIO_ANONIMO.equals(this.tipoUsuario);
    }

    public boolean podeCriar(TipoSolicitacao tipoSolicitacao){
        return this.tipoUsuario.podeCriar(tipoSolicitacao);
    }

    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }
    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }
    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public LocalDateTime getUsuarioDataNascimento() {
        return usuarioDataNascimento;
    }

    public String getUsuarioSenha(){return usuarioSenha;}

    public void setUsuarioSenha(String usuarioSenha) {
        this.usuarioSenha = usuarioSenha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
