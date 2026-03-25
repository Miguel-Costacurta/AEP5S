import Util.GeradorProtocolo;
import enums.TipoUsuario;
import model.Solicitacao;
import model.Usuario;

import java.util.Scanner;

public class Main {

    static void main(String[] args) {

        private GeradorProtocolo geradorProtocolo;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem vindo a central de solicitações!");
        System.out.printf("Deseja fazer uma solicitação anonima?\n1 - SIM\n2 - NÃO\n");

        int op = scanner.nextInt();

        switch (op){
            case 1:
                Usuario usuario = new Usuario(TipoUsuario.USUARIO_ANONIMO);
                System.out.printf("Qual seria a sua solicitação?\n 1 - SOLICITACAO_BURACAO\n" +
                        " 2 - SOLICITACAO PODA DE ARVORE\n" +
                        " 3 - INFORMAR SOBRE QUEDA DE ARVORE\n" +
                        " 4 - SOLICITACAR REFORMA\n" +
                        " 5 - DENUNCIAR ASSEDIO\n" +
                        " 6 - ILUMINACAO PUBLICA\n" +
                        " 0 - SAIR");

                int opSolicitacao = scanner.nextInt();
                if (opSolicitacao == 0){
                    System.exit(0);
                }else {
                    System.out.printf("Numero do seu protocolo para acompanhar solicitação: %s", geradorProtocolo.getProtocoSolicitacao(););
                    Solicitacao solicitacao = new Solicitacao(geradorProtocolo.getProtocoSolicitacao(),)
                }
                break;
            case 2:
                break;
        }

        //System.out.println("Por favor informe seu login e senha:");

    }
}
