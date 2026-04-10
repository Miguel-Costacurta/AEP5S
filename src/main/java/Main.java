import Service.AuthService;
import Service.ServicoSolicitacoes;
import dao.HistoricoStatusDAO;
import dao.SolicitacaoDAO;
import dao.UsuarioDAO;
import db.DataBaseConfig;
import enums.Prioridade;
import enums.StatusSolicitacao;
import enums.TipoSolicitacao;
import enums.TipoUsuario;
import model.HistoricoStatus;
import model.Solicitacao;
import model.Usuario;
import Util.GeradorProtocolo;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataBaseConfig.initDatabase();

        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        HistoricoStatusDAO historicoDAO = new HistoricoStatusDAO();
        ServicoSolicitacoes servicoSolicitacoes = new ServicoSolicitacoes();
        try {
            usuarioDAO.criarUsuariosPadrao();

            boolean executando = true;

            while (executando) {
                System.out.println("\n=== CENTRAL DE SOLICITAÇÕES ===");
                System.out.println("1 - Fazer solicitação");
                System.out.println("2 - Área do atendente");
                System.out.println("3 - Área do gestor");
                System.out.println("4 - Acompanhar solicitação por protocolo");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");

                int op = Integer.parseInt(scanner.nextLine());

                switch (op) {
                    case 1:
                        menuUsuario(scanner, authService, solicitacaoDAO);
                        break;
                    case 2:
                        menuAtendente(scanner, authService, solicitacaoDAO, servicoSolicitacoes);
                        break;
                    case 3:
                        menuGestor(scanner, authService, solicitacaoDAO, servicoSolicitacoes);
                        break;
                    case 4:
                        acompanharSolicitacao(scanner, solicitacaoDAO, historicoDAO);
                        break;
                    case 0:
                        executando = false;
                        System.out.println("Sistema encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no sistema: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void menuUsuario(Scanner scanner, AuthService authService, SolicitacaoDAO solicitacaoDAO) throws SQLException {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n=== ÁREA DE LOGIN ===");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastrar usuário comum");
            System.out.println("3 - Continuar sem login (Anônimo)");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1:
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    Usuario usuario = authService.login(email, senha);

                    if (usuario == null) break;

                    if (usuario.getTipoUsuario() != TipoUsuario.USUARIO_LOGADO) {
                        System.out.println("Essa área é apenas para usuários comuns.");
                        break;
                    }

                    boolean menuLogado = true;
                    while (menuLogado) {
                        System.out.println("\n=== USUÁRIO LOGADO: " + usuario.getUsuarioNome() + " ===");
                        System.out.println("1 - Criar solicitação");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        int acao = Integer.parseInt(scanner.nextLine());

                        switch (acao) {
                            case 1:
                                criarSolicitacao(scanner, solicitacaoDAO, usuario);
                                break;
                            case 0:
                                menuLogado = false;
                                System.out.println("Logout realizado.");
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    }
                    break;

                case 2:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String emailCadastro = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senhaCadastro = scanner.nextLine();
                    authService.cadastrarUsuarioLogado(nome, emailCadastro, senhaCadastro);
                    break;

                case 3:
                    Usuario usuarioAnonimo = new Usuario(TipoUsuario.USUARIO_ANONIMO);
                    System.out.println("=== SOLICITAÇÃO ANÔNIMA ===");
                    criarSolicitacao(scanner, solicitacaoDAO, usuarioAnonimo);
                    break;

                case 0:
                    voltar = true;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAtendente(Scanner scanner, AuthService authService, SolicitacaoDAO solicitacaoDAO, ServicoSolicitacoes servicoSolicitacoes) throws SQLException {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n=== ÁREA DO ATENDENTE ===");
            System.out.println("1 - Login de atendente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1:
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    Usuario usuario = authService.login(email, senha);

                    if (usuario == null || usuario.getTipoUsuario() != TipoUsuario.USUARIO_ATENDENTE) {
                        System.out.println("Login inválido para atendente.");
                        break;
                    }

                    boolean menuAtendenteLogado = true;
                    while (menuAtendenteLogado) {
                        System.out.println("\n=== MENU ATENDENTE: " + usuario.getUsuarioNome() + " ===");
                        System.out.println("1 - Listar pendentes");
                        System.out.println("2 - Colocar em atendimento");
                        System.out.println("3 - Aguardar resposta");
                        System.out.println("4 - Concluir solicitação");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        int acao = Integer.parseInt(scanner.nextLine());

                        switch (acao) {
                            case 1:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE));
                                break;

                            case 2:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE));
                                System.out.print("ID da solicitação: ");
                                int idAtender = Integer.parseInt(scanner.nextLine());
                                Solicitacao sAtender = solicitacaoDAO.buscarPorId(idAtender);
                                if (sAtender == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsAtender = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sAtender, StatusSolicitacao.EM_ATENDIMENTO, usuario, obsAtender);
                                    System.out.println("Solicitação em atendimento.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 3:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.EM_ATENDIMENTO));
                                System.out.print("ID da solicitação: ");
                                int idAguardar = Integer.parseInt(scanner.nextLine());
                                Solicitacao sAguardar = solicitacaoDAO.buscarPorId(idAguardar);
                                if (sAguardar == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsAguardar = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sAguardar, StatusSolicitacao.AGUARDANDO_RESPOSTA, usuario, obsAguardar);
                                    System.out.println("Solicitação aguardando resposta.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 4:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("ID da solicitação: ");
                                int idConcluir = Integer.parseInt(scanner.nextLine());
                                Solicitacao sConcluir = solicitacaoDAO.buscarPorId(idConcluir);
                                if (sConcluir == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsConcluir = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sConcluir, StatusSolicitacao.CONCLUIDA, usuario, obsConcluir);
                                    System.out.println("Solicitação concluída.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 0:
                                menuAtendenteLogado = false;
                                System.out.println("Logout realizado.");
                                break;

                            default:
                                System.out.println("Opção inválida.");
                        }
                    }
                    break;

                case 0:
                    voltar = true;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuGestor(Scanner scanner, AuthService authService, SolicitacaoDAO solicitacaoDAO, ServicoSolicitacoes servicoSolicitacoes) throws SQLException {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n=== ÁREA DO GESTOR ===");
            System.out.println("1 - Login de gestor");
            System.out.println("2 - Cadastrar gestor");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1:
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    Usuario usuario = authService.login(email, senha);

                    if (usuario == null || usuario.getTipoUsuario() != TipoUsuario.USUARIO_GESTOR) {
                        System.out.println("Login inválido para gestor.");
                        break;
                    }

                    boolean menuGestorLogado = true;
                    while (menuGestorLogado) {
                        System.out.println("\n=== MENU GESTOR: " + usuario.getUsuarioNome() + " ===");
                        System.out.println("1 - Listar solicitações");
                        System.out.println("2 - Colocar em atendimento");
                        System.out.println("3 - Concluir");
                        System.out.println("4 - Cancelar");
                        System.out.println("5 - Cadastrar atendente");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        int acao = Integer.parseInt(scanner.nextLine());

                        switch (acao) {
                            case 1:
                                System.out.println("\nFiltrar por:");
                                System.out.println("1 - Todas");
                                System.out.println("2 - Por status");
                                System.out.println("3 - Por prioridade");
                                System.out.println("4 - Por categoria");
                                System.out.println("5 - Por bairro");
                                System.out.print("Escolha: ");
                                int filtro = Integer.parseInt(scanner.nextLine());

                                switch (filtro) {
                                    case 1:
                                        listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                        break;
                                    case 2:
                                        System.out.println("1-PENDENTE  2-EM_ATENDIMENTO  3-AGUARDANDO_RESPOSTA  4-CONCLUIDA  5-CANCELADA");
                                        int opStatus = Integer.parseInt(scanner.nextLine());
                                        StatusSolicitacao[] statuses = StatusSolicitacao.values();
                                        if (opStatus >= 1 && opStatus <= statuses.length)
                                            listarSolicitacoes(solicitacaoDAO.listarPorStatus(statuses[opStatus - 1]));
                                        break;
                                    case 3:
                                        System.out.println("1-ALTA  2-MEDIA  3-BAIXA");
                                        int opPrioridade = Integer.parseInt(scanner.nextLine());
                                        Prioridade p = opPrioridade == 1 ? Prioridade.ALTA : opPrioridade == 2 ? Prioridade.MEDIA : Prioridade.BAIXA;
                                        listarSolicitacoes(solicitacaoDAO.listarPorPrioridade(p));
                                        break;
                                    case 4:
                                        System.out.println("1-DENUNCIA_BURACO  2-SOLICITACAO_PODA  3-SOLICITACAO_ARVORE_CAIDA");
                                        System.out.println("4-SOLICITACAO_REFORMA  5-DENUNCIA_MAUS_TRATOS  6-SOLICITACAO_ILUMINACAO");
                                        int opTipo = Integer.parseInt(scanner.nextLine());
                                        TipoSolicitacao[] tipos = TipoSolicitacao.values();
                                        if (opTipo >= 1 && opTipo <= tipos.length)
                                            listarSolicitacoes(solicitacaoDAO.listarPorTipo(tipos[opTipo - 1]));
                                        break;
                                    case 5:
                                        System.out.print("Bairro: ");
                                        String bairro = scanner.nextLine();
                                        listarSolicitacoes(solicitacaoDAO.listarPorLocalizacao(bairro));
                                        break;
                                    default:
                                        System.out.println("Opção inválida.");
                                }
                                break;

                            case 2:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE));
                                System.out.print("ID da solicitação: ");
                                int idAtender = Integer.parseInt(scanner.nextLine());
                                Solicitacao sAtender = solicitacaoDAO.buscarPorId(idAtender);
                                if (sAtender == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsAtender = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sAtender, StatusSolicitacao.EM_ATENDIMENTO, usuario, obsAtender);
                                    System.out.println("Solicitação em atendimento.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 3:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("ID da solicitação: ");
                                int idConcluir = Integer.parseInt(scanner.nextLine());
                                Solicitacao sConcluir = solicitacaoDAO.buscarPorId(idConcluir);
                                if (sConcluir == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsConcluir = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sConcluir, StatusSolicitacao.CONCLUIDA, usuario, obsConcluir);
                                    System.out.println("Solicitação concluída.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 4:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("ID da solicitação: ");
                                int idCancelar = Integer.parseInt(scanner.nextLine());
                                Solicitacao sCancelar = solicitacaoDAO.buscarPorId(idCancelar);
                                if (sCancelar == null) { System.out.println("Não encontrada."); break; }
                                System.out.print("Observação (obrigatório): ");
                                String obsCancelar = scanner.nextLine();
                                try {
                                    servicoSolicitacoes.mudarStatus(sCancelar, StatusSolicitacao.CANCELADA, usuario, obsCancelar);
                                    System.out.println("Solicitação cancelada.");
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                break;

                            case 5:
                                System.out.print("Nome do atendente: ");
                                String nomeAtendente = scanner.nextLine();
                                System.out.print("Email: ");
                                String emailAtendente = scanner.nextLine();
                                System.out.print("Senha: ");
                                String senhaAtendente = scanner.nextLine();
                                authService.cadastrarAtendente(nomeAtendente, emailAtendente, senhaAtendente);
                                break;

                            case 0:
                                menuGestorLogado = false;
                                System.out.println("Logout realizado.");
                                break;

                            default:
                                System.out.println("Opção inválida.");
                        }
                    }
                    break;

                case 2:
                    System.out.print("Nome do gestor: ");
                    String nomeGestor = scanner.nextLine();
                    System.out.print("Email do gestor: ");
                    String emailGestor = scanner.nextLine();
                    System.out.print("Senha do gestor: ");
                    String senhaGestor = scanner.nextLine();
                    authService.cadastrarGestor(nomeGestor, emailGestor, senhaGestor);
                    break;

                case 0:
                    voltar = true;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void criarSolicitacao(Scanner scanner, SolicitacaoDAO solicitacaoDAO, Usuario usuario) throws SQLException {
        boolean continuar = true;

        while (continuar) {
            GeradorProtocolo protocolo = new GeradorProtocolo(usuario);

            System.out.println("\n=== CRIAR SOLICITAÇÃO ===");
            System.out.println("1 - DENUNCIA_BURACO");
            System.out.println("2 - SOLICITACAO_PODA");
            System.out.println("3 - SOLICITACAO_ARVORE_CAIDA");
            System.out.println("4 - SOLICITACAO_REFORMA");
            System.out.println("5 - DENUNCIA_MAUS_TRATOS");
            System.out.println("6 - SOLICITACAO_ILUMINACAO");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            int opSolicitacao = Integer.parseInt(scanner.nextLine());

            if (opSolicitacao == 0) return;

            TipoSolicitacao tipoSolicitacao = null;
            switch (opSolicitacao) {
                case 1: tipoSolicitacao = TipoSolicitacao.DENUNCIA_BURACO; break;
                case 2: tipoSolicitacao = TipoSolicitacao.SOLICITACAO_PODA; break;
                case 3: tipoSolicitacao = TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA; break;
                case 4: tipoSolicitacao = TipoSolicitacao.SOLICITACAO_REFORMA; break;
                case 5: tipoSolicitacao = TipoSolicitacao.DENUNCIA_MAUS_TRATOS; break;
                case 6: tipoSolicitacao = TipoSolicitacao.SOLICITACAO_ILUMINACAO; break;
                default:
                    System.out.println("Opção inválida.");
                    continue;
            }

            if (!usuario.podeCriar(tipoSolicitacao)) {
                System.out.println("Seu tipo de usuário não pode criar essa solicitação.");
                continue;
            }

            System.out.print("Descreva a solicitação: ");
            String descricao = scanner.nextLine();

            System.out.print("Informe o bairro/localização: ");
            String localizacao = scanner.nextLine();

            System.out.println("Prioridade:");
            System.out.println("1 - ALTA  (prazo: 2 dias)");
            System.out.println("2 - MEDIA (prazo: 5 dias)");
            System.out.println("3 - BAIXA (prazo: 10 dias)");
            System.out.print("Escolha: ");
            int opPrioridade = Integer.parseInt(scanner.nextLine());
            Prioridade prioridade = opPrioridade == 1 ? Prioridade.ALTA : opPrioridade == 2 ? Prioridade.MEDIA : Prioridade.BAIXA;

            Solicitacao solicitacao = new Solicitacao(
                    protocolo.getProtocolo(),
                    tipoSolicitacao,
                    prioridade,
                    descricao,
                    localizacao,
                    usuario.getUsuarioId()
            );

            solicitacaoDAO.criarSolicitacao(solicitacao);

            System.out.println("\nSolicitação criada com sucesso!");
            System.out.println("Protocolo : " + solicitacao.getSolicitacaoProtocolo());
            System.out.println("Prazo     : " + solicitacao.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            System.out.println("\nDeseja criar outra solicitação? (1-Sim / 2-Não)");
            int resposta = Integer.parseInt(scanner.nextLine());
            if (resposta != 1) continuar = false;
        }
    }

    private static void listarSolicitacoes(List<Solicitacao> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Solicitacao s : lista) {
            System.out.println("--------------------------------");
            System.out.println("ID        : " + s.getSolicitacaoId());
            System.out.println("Protocolo : " + s.getSolicitacaoProtocolo());
            System.out.println("Tipo      : " + s.getTipoSolicitacao());
            System.out.println("Status    : " + s.getStatusSolicitacao());
            System.out.println("Prioridade: " + s.getPrioridade());
            System.out.println("Localiz.  : " + s.getLocalizacao());
            System.out.println("Descrição : " + s.getDescricao());
            if (s.getPrazo() != null) {
                System.out.println("Prazo     : " + s.getPrazo().format(fmt));
                if (s.estaAtrasada()) System.out.println("⚠ EM ATRASO");
            }
        }
        System.out.println("--------------------------------");
    }

    // ===================== ACOMPANHAR POR PROTOCOLO =====================

    private static void acompanharSolicitacao(Scanner scanner, SolicitacaoDAO solicitacaoDAO,
                                              HistoricoStatusDAO historicoDAO) throws SQLException {
        System.out.print("\nDigite o protocolo: ");
        String protocolo = scanner.nextLine();

        Solicitacao s = solicitacaoDAO.buscarPorProtocolo(protocolo);

        if (s == null) {
            System.out.println("Protocolo não encontrado.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\n=== SOLICITAÇÃO ===");
        System.out.println("Protocolo : " + s.getSolicitacaoProtocolo());
        System.out.println("Tipo      : " + s.getTipoSolicitacao());
        System.out.println("Status    : " + s.getStatusSolicitacao());
        System.out.println("Prioridade: " + s.getPrioridade());
        System.out.println("Localiz.  : " + s.getLocalizacao());
        System.out.println("Descrição : " + s.getDescricao());
        if (s.getPrazo() != null) {
            System.out.println("Prazo     : " + s.getPrazo().format(fmt));
            if (s.estaAtrasada()) {
                System.out.println("⚠ SOLICITAÇÃO EM ATRASO — aguardando justificativa do responsável.");
            }
        }

        List<HistoricoStatus> historico = historicoDAO.buscarPorSolicitacao(s.getSolicitacaoId());

        if (historico.isEmpty()) {
            System.out.println("\nNenhuma movimentação registrada ainda.");
        } else {
            System.out.println("\n=== HISTÓRICO DE MOVIMENTAÇÕES ===");
            for (HistoricoStatus h : historico) {
                System.out.println("  " + h.getDataAlteracao().format(fmt)
                        + " | " + h.getStatusAnterior()
                        + " → " + h.getStatusAtual()
                        + " | " + h.getObservacao());
            }
        }
    }
}