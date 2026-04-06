import Service.ServicoSolicitacoes;
import Util.GeradorProtocolo;
import dao.ComentarioDAO;
import dao.HistoricoStatusDAO;
import dao.SolicitacaoDAO;
import dao.UsuarioDAO;
import db.DataBaseConfig;
import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import enums.TipoUsuario;
import model.Comentario;
import model.HistoricoStatus;
import model.Solicitacao;
import model.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataBaseConfig.initDatabase();

        Scanner scanner = new Scanner(System.in);
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

        System.out.println("Bem vindo a central de solicitações!");
        System.out.println("1 - Fazer solicitação anônima");
        System.out.println("2 - Fazer solicitação com cadastro");
        System.out.println("3 - Entrar no menu do gestor");
        System.out.println("4 - Adicionar comentário em uma solicitação");
        System.out.println("5 - Listar minhas solicitações");
        System.out.println("0 - Sair");

        int op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1 -> {
                Usuario usuarioAnonimo = new Usuario(TipoUsuario.USUARIO_ANONIMO);
                GeradorProtocolo protocoloAnonimo = new GeradorProtocolo(usuarioAnonimo);

                TipoSolicitacao tipoAnonimo = escolherTipoSolicitacaoAnonima(scanner, usuarioAnonimo);

                if (tipoAnonimo == null) {
                    System.out.println("Solicitação encerrada.");
                    return;
                }

                System.out.println("Descreva sua solicitação:");
                String descricaoAnonima = scanner.nextLine();

                Solicitacao solicitacaoAnonima = new Solicitacao(
                        protocoloAnonimo.getProtocolo(),
                        tipoAnonimo,
                        Prioridade.ALTA,
                        descricaoAnonima,
                        usuarioAnonimo.getUsuarioId()
                );

                try {
                    solicitacaoDAO.criarSolicitacao(solicitacaoAnonima);
                    System.out.println("Solicitação anônima criada com sucesso!");
                    System.out.println("Protocolo: " + protocoloAnonimo.getProtocolo());
                } catch (SQLException e) {
                    System.out.println("Erro ao salvar solicitação anônima: " + e.getMessage());
                }
            }

            case 2 -> {
                System.out.println("=== CADASTRO DE USUÁRIO ===");

                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                System.out.println("Selecione o perfil do usuário:");
                System.out.println("1 - Usuário logado");
                System.out.println("2 - Baixa renda");
                System.out.println("3 - Vulnerabilidade / receio de retaliação");
                System.out.println("4 - Servidor");

                int opPerfil = scanner.nextInt();
                scanner.nextLine();

                TipoUsuario tipoUsuario = switch (opPerfil) {
                    case 1 -> TipoUsuario.USUARIO_LOGADO;
                    case 2 -> TipoUsuario.USUARIO_BAIXA_RENDA;
                    case 3 -> TipoUsuario.USUARIO_VULNERABILIDADE;
                    case 4 -> TipoUsuario.USUARIO_SERVIDOR;
                    default -> null;
                };

                if (tipoUsuario == null) {
                    System.out.println("Perfil inválido.");
                    return;
                }

                Usuario usuarioCadastrado = new Usuario(
                        nome,
                        tipoUsuario,
                        senha,
                        email,
                        LocalDateTime.now()
                );

                try {
                    usuarioDAO.salvarUsuario(usuarioCadastrado);
                    System.out.println("Usuário cadastrado com sucesso!");
                } catch (SQLException e) {
                    System.out.println("Erro ao salvar usuário: " + e.getMessage());
                    return;
                }

                GeradorProtocolo protocoloCadastro = new GeradorProtocolo(usuarioCadastrado);

                TipoSolicitacao tipoSolicitacao = escolherTipoSolicitacao(scanner, usuarioCadastrado);

                if (tipoSolicitacao == null) {
                    System.out.println("Solicitação encerrada.");
                    return;
                }

                System.out.println("Descreva sua solicitação:");
                String descricao = scanner.nextLine();

                Solicitacao solicitacao = new Solicitacao(
                        protocoloCadastro.getProtocolo(),
                        tipoSolicitacao,
                        Prioridade.ALTA,
                        descricao,
                        usuarioCadastrado.getUsuarioId()
                );

                try {
                    solicitacaoDAO.criarSolicitacao(solicitacao);
                    System.out.println("Solicitação com cadastro criada com sucesso!");
                    System.out.println("Protocolo: " + protocoloCadastro.getProtocolo());
                } catch (SQLException e) {
                    System.out.println("Erro ao salvar solicitação: " + e.getMessage());
                }
            }

            case 3 -> {
                Usuario gestor = new Usuario(
                        "Gestor",
                        TipoUsuario.USUARIO_GESTOR,
                        "123",
                        "gestor@email.com",
                        LocalDateTime.now()
                );
                gestor.setUsuarioId(1);
                menuGestor(scanner, gestor);
            }

            case 4 -> {
                System.out.println("=== ADICIONAR COMENTÁRIO ===");

                System.out.print("Informe seu nome: ");
                String nomeComentario = scanner.nextLine();

                System.out.print("Informe seu email: ");
                String emailComentario = scanner.nextLine();

                System.out.print("Informe sua senha: ");
                String senhaComentario = scanner.nextLine();

                System.out.println("Selecione o perfil do usuário:");
                System.out.println("1 - Usuário logado");
                System.out.println("2 - Baixa renda");
                System.out.println("3 - Vulnerabilidade / receio de retaliação");
                System.out.println("4 - Servidor");
                System.out.println("5 - Atendente");

                int opPerfilComentario = scanner.nextInt();
                scanner.nextLine();

                TipoUsuario tipoComentario = switch (opPerfilComentario) {
                    case 1 -> TipoUsuario.USUARIO_LOGADO;
                    case 2 -> TipoUsuario.USUARIO_BAIXA_RENDA;
                    case 3 -> TipoUsuario.USUARIO_VULNERABILIDADE;
                    case 4 -> TipoUsuario.USUARIO_SERVIDOR;
                    case 5 -> TipoUsuario.USUARIO_ATENDENTE;
                    default -> null;
                };

                if (tipoComentario == null) {
                    System.out.println("Perfil inválido.");
                    return;
                }

                Usuario usuarioComentario = new Usuario(
                        nomeComentario,
                        tipoComentario,
                        senhaComentario,
                        emailComentario,
                        LocalDateTime.now()
                );

                try {
                    usuarioDAO.salvarUsuario(usuarioComentario);
                } catch (SQLException e) {
                    System.out.println("Erro ao salvar usuário do comentário: " + e.getMessage());
                    return;
                }

                System.out.print("Informe o protocolo da solicitação: ");
                String protocoloComentario = scanner.nextLine();

                try {
                    Solicitacao solicitacaoComentario = solicitacaoDAO.buscarPorProtocolo(protocoloComentario);

                    if (solicitacaoComentario == null) {
                        System.out.println("Solicitação não encontrada.");
                        return;
                    }

                    System.out.print("Digite o comentário: ");
                    String textoComentario = scanner.nextLine();

                    ServicoSolicitacoes servico = new ServicoSolicitacoes();
                    servico.adicionarComentario(solicitacaoComentario, usuarioComentario, textoComentario);

                    System.out.println("Comentário registrado com sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro ao registrar comentário: " + e.getMessage());
                }
            }

            case 5 -> {
                System.out.println("=== LISTAR SOLICITAÇÕES DO USUÁRIO ===");

                System.out.print("Informe o ID do usuário: ");
                int usuarioIdConsulta = scanner.nextInt();
                scanner.nextLine();

                try {
                    List<Solicitacao> minhasSolicitacoes = solicitacaoDAO.listarPorUsuario(usuarioIdConsulta);

                    if (minhasSolicitacoes.isEmpty()) {
                        System.out.println("Nenhuma solicitação encontrada para este usuário.");
                    } else {
                        for (Solicitacao s : minhasSolicitacoes) {
                            System.out.println("-----------------------------------");
                            System.out.println("ID: " + s.getSolicitacaoId());
                            System.out.println("Protocolo: " + s.getSolicitacaoProtocolo());
                            System.out.println("Tipo: " + s.getTipoSolicitacao());
                            System.out.println("Status: " + s.getStatusSolicitacao());
                            System.out.println("Prioridade: " + s.getPrioridade());
                            System.out.println("Descrição: " + s.getDescricao());
                            System.out.println("Data criação: " + s.getDataCriacao());
                            System.out.println("Data atualização: " + s.getDataAtualizacao());
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao listar solicitações do usuário: " + e.getMessage());
                }
            }

            case 0 -> System.out.println("Encerrando sistema...");
            default -> System.out.println("Opção inválida.");
        }
    }

    public static TipoSolicitacao escolherTipoSolicitacao(Scanner scanner, Usuario usuario) {
        System.out.println("Qual seria a sua solicitação?");
        System.out.println("1 - DENUNCIA_BURACO");
        System.out.println("2 - SOLICITACAO_PODA");
        System.out.println("3 - SOLICITACAO_ARVORE_CAIDA");
        System.out.println("4 - SOLICITACAO_REFORMA");
        System.out.println("5 - DENUNCIA_ASSEDIO");
        System.out.println("6 - SOLICITACAO_ILUMINACAO");
        System.out.println("0 - SAIR");

        int opSolicitacao = scanner.nextInt();
        scanner.nextLine();

        TipoSolicitacao tipo = switch (opSolicitacao) {
            case 1 -> TipoSolicitacao.DENUNCIA_BURACO;
            case 2 -> TipoSolicitacao.SOLICITACAO_PODA;
            case 3 -> TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA;
            case 4 -> TipoSolicitacao.SOLICITACAO_REFORMA;
            case 5 -> TipoSolicitacao.DENUNCIA_ASSEDIO;
            case 6 -> TipoSolicitacao.SOLICITACAO_ILUMINACAO;
            case 0 -> null;
            default -> null;
        };

        if (tipo == null) {
            return null;
        }

        if (!usuario.podeCriar(tipo)) {
            System.out.println("Esse perfil não pode criar esse tipo de solicitação.");
            return null;
        }

        return tipo;
    }

    public static TipoSolicitacao escolherTipoSolicitacaoAnonima(Scanner scanner, Usuario usuario) {
        System.out.println("Qual seria a sua solicitação?");
        System.out.println("1 - DENUNCIA_BURACO");
        System.out.println("2 - SOLICITACAO_PODA");
        System.out.println("3 - SOLICITACAO_ARVORE_CAIDA");
        System.out.println("4 - SOLICITACAO_REFORMA");
        System.out.println("5 - SOLICITACAO_ILUMINACAO");
        System.out.println("0 - SAIR");

        int opSolicitacao = scanner.nextInt();
        scanner.nextLine();

        TipoSolicitacao tipo = switch (opSolicitacao) {
            case 1 -> TipoSolicitacao.DENUNCIA_BURACO;
            case 2 -> TipoSolicitacao.SOLICITACAO_PODA;
            case 3 -> TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA;
            case 4 -> TipoSolicitacao.SOLICITACAO_REFORMA;
            case 5 -> TipoSolicitacao.SOLICITACAO_ILUMINACAO;
            case 0 -> null;
            default -> null;
        };

        if (tipo == null) {
            return null;
        }

        if (!usuario.podeCriar(tipo)) {
            System.out.println("Usuário anônimo não pode criar esse tipo de solicitação.");
            return null;
        }

        return tipo;
    }

    public static void menuGestor(Scanner scanner, Usuario gestor) {
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        HistoricoStatusDAO historicoDAO = new HistoricoStatusDAO();
        ComentarioDAO comentarioDAO = new ComentarioDAO();
        ServicoSolicitacoes servico = new ServicoSolicitacoes();

        int op = -1;

        while (op != 0) {
            System.out.println("\n=== MENU GESTOR ===");
            System.out.println("1 - Listar todas as solicitações");
            System.out.println("2 - Buscar solicitação por protocolo");
            System.out.println("3 - Alterar status de solicitação");
            System.out.println("4 - Cancelar solicitação");
            System.out.println("5 - Ver histórico de status");
            System.out.println("6 - Listagem de solicitações do atendente");
            System.out.println("7 - Ver comentários");
            System.out.println("0 - Sair");

            op = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (op) {
                    case 1 -> {
                        List<Solicitacao> lista = solicitacaoDAO.listarTodas();

                        if (lista.isEmpty()) {
                            System.out.println("Nenhuma solicitação cadastrada.");
                        } else {
                            for (Solicitacao s : lista) {
                                System.out.println("-----------------------------------");
                                System.out.println("ID: " + s.getSolicitacaoId());
                                System.out.println("Protocolo: " + s.getSolicitacaoProtocolo());
                                System.out.println("Tipo: " + s.getTipoSolicitacao());
                                System.out.println("Status: " + s.getStatusSolicitacao());
                                System.out.println("Prioridade: " + s.getPrioridade());
                                System.out.println("Descrição: " + s.getDescricao());
                                System.out.println("Usuário ID: " + s.getUsuarioId());
                            }
                        }
                    }

                    case 2 -> {
                        System.out.print("Informe o protocolo: ");
                        String protocolo = scanner.nextLine();

                        Solicitacao s = solicitacaoDAO.buscarPorProtocolo(protocolo);

                        if (s == null) {
                            System.out.println("Solicitação não encontrada.");
                        } else {
                            System.out.println("ID: " + s.getSolicitacaoId());
                            System.out.println("Protocolo: " + s.getSolicitacaoProtocolo());
                            System.out.println("Tipo: " + s.getTipoSolicitacao());
                            System.out.println("Status: " + s.getStatusSolicitacao());
                            System.out.println("Prioridade: " + s.getPrioridade());
                            System.out.println("Descrição: " + s.getDescricao());
                        }
                    }

                    case 3 -> {
                        System.out.print("Informe o protocolo: ");
                        String protocolo = scanner.nextLine();

                        Solicitacao s = solicitacaoDAO.buscarPorProtocolo(protocolo);

                        if (s == null) {
                            System.out.println("Solicitação não encontrada.");
                            break;
                        }

                        System.out.println("Novo status:");
                        System.out.println("1 - EM_ATENDIMENTO");
                        System.out.println("2 - AGUARDANDO_RESPOSTA");
                        System.out.println("3 - CONCLUIDA");

                        int opStatus = scanner.nextInt();
                        scanner.nextLine();

                        StatusSolicitacao novoStatus = switch (opStatus) {
                            case 1 -> StatusSolicitacao.EM_ATENDIMENTO;
                            case 2 -> StatusSolicitacao.AGUARDANDO_RESPOSTA;
                            case 3 -> StatusSolicitacao.CONCLUIDA;
                            default -> null;
                        };

                        if (novoStatus == null) {
                            System.out.println("Status inválido.");
                            break;
                        }

                        System.out.print("Observação: ");
                        String observacao = scanner.nextLine();

                        servico.mudarStatus(s, novoStatus, gestor, observacao);
                        System.out.println("Status alterado com sucesso.");
                    }

                    case 4 -> {
                        System.out.print("Informe o protocolo: ");
                        String protocolo = scanner.nextLine();

                        Solicitacao s = solicitacaoDAO.buscarPorProtocolo(protocolo);

                        if (s == null) {
                            System.out.println("Solicitação não encontrada.");
                            break;
                        }

                        System.out.print("Motivo do cancelamento: ");
                        String observacao = scanner.nextLine();

                        servico.mudarStatus(s, StatusSolicitacao.CANCELADA, gestor, observacao);
                        System.out.println("Solicitação cancelada com sucesso.");
                    }

                    case 5 -> {
                        System.out.print("Informe o ID da solicitação: ");
                        int solicitacaoId = scanner.nextInt();
                        scanner.nextLine();

                        List<HistoricoStatus> historicos = historicoDAO.buscarPorSolicitacao(solicitacaoId);

                        if (historicos.isEmpty()) {
                            System.out.println("Nenhum histórico encontrado.");
                        } else {
                            for (HistoricoStatus h : historicos) {
                                System.out.println("-----------------------------------");
                                System.out.println("Status anterior: " + h.getStatusAnterior());
                                System.out.println("Status novo: " + h.getStatusAtual());
                                System.out.println("Observação: " + h.getObservacao());
                                System.out.println("Data: " + h.getDataAlteracao());
                                System.out.println("Usuário ID: " + h.getUsuarioId());
                            }
                        }
                    }

                    case 6 -> {
                        System.out.println("=== LISTAGEM DE SOLICITAÇÕES DO ATENDENTE ===");
                        System.out.println("1 - Listar todas");
                        System.out.println("2 - Listar somente PENDENTES");
                        System.out.println("3 - Listar EM_ATENDIMENTO");
                        System.out.println("4 - Listar AGUARDANDO_RESPOSTA");
                        System.out.println("5 - Listar CONCLUIDAS");

                        int opAtendente = scanner.nextInt();
                        scanner.nextLine();

                        List<Solicitacao> solicitacoesAtendente = null;

                        switch (opAtendente) {
                            case 1 -> solicitacoesAtendente = solicitacaoDAO.listarTodas();
                            case 2 -> solicitacoesAtendente = solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE);
                            case 3 -> solicitacoesAtendente = solicitacaoDAO.listarPorStatus(StatusSolicitacao.EM_ATENDIMENTO);
                            case 4 -> solicitacoesAtendente = solicitacaoDAO.listarPorStatus(StatusSolicitacao.AGUARDANDO_RESPOSTA);
                            case 5 -> solicitacoesAtendente = solicitacaoDAO.listarPorStatus(StatusSolicitacao.CONCLUIDA);
                            default -> System.out.println("Opção inválida.");
                        }

                        if (solicitacoesAtendente != null) {
                            if (solicitacoesAtendente.isEmpty()) {
                                System.out.println("Nenhuma solicitação encontrada.");
                            } else {
                                for (Solicitacao s : solicitacoesAtendente) {
                                    System.out.println("-----------------------------------");
                                    System.out.println("ID: " + s.getSolicitacaoId());
                                    System.out.println("Protocolo: " + s.getSolicitacaoProtocolo());
                                    System.out.println("Tipo: " + s.getTipoSolicitacao());
                                    System.out.println("Status: " + s.getStatusSolicitacao());
                                    System.out.println("Prioridade: " + s.getPrioridade());
                                    System.out.println("Descrição: " + s.getDescricao());
                                    System.out.println("Usuário ID: " + s.getUsuarioId());
                                    System.out.println("Data criação: " + s.getDataCriacao());
                                    System.out.println("Data atualização: " + s.getDataAtualizacao());
                                }
                            }
                        }
                    }

                    case 7 -> {
                        System.out.print("Informe o ID da solicitação: ");
                        int solicitacaoId = scanner.nextInt();
                        scanner.nextLine();

                        List<Comentario> comentarios = comentarioDAO.buscarPorSolicitacao(solicitacaoId);

                        if (comentarios.isEmpty()) {
                            System.out.println("Nenhum comentário encontrado.");
                        } else {
                            for (Comentario c : comentarios) {
                                System.out.println("-----------------------------------");
                                System.out.println("Comentário ID: " + c.getComentarioId());
                                System.out.println("Texto: " + c.getTexto());
                                System.out.println("Data: " + c.getDataCriacao());
                                System.out.println("Usuário ID: " + c.getUsuarioId());
                            }
                        }
                    }

                    case 0 -> System.out.println("Saindo do menu do gestor...");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}