package Util;

import model.Solicitacao;
import model.Usuario;

import java.time.LocalDateTime;

public class GeradorProtocolo {
    private LocalDateTime data = LocalDateTime.now();
    private Usuario usuario;

    public GeradorProtocolo(Usuario usuario){
        this.usuario = usuario;
    }

    public String getProtocolo(){

        String protocolo = "OS"+data.getYear()+ data.getMonthValue()+ data.getDayOfMonth() + usuario.getUsuarioId();

        return protocolo;
    }


}
