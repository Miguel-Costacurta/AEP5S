package model;

import enums.TipoSolicitacao;
import enums.TipoUsuario;

public class Usuario {
    private int usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private String usuarioDataNascimento;

    private TipoUsuario tipoUsuario;

    public Usuario(){}

    public Usuario(TipoUsuario tipo){
        this.tipoUsuario = tipo;
    }

    public Usuario(String nome, TipoUsuario tipo){
        this.usuarioNome = nome;
        this.tipoUsuario = tipo;
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

    public String getUsuarioDataNascimento() {
        return usuarioDataNascimento;
    }
    public void setUsuarioDataNascimento(String usuarioDataNascimento) {
        this.usuarioDataNascimento = usuarioDataNascimento;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
