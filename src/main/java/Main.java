import Util.GeradorProtocolo;
import Service.AuthService;
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
import dao.ComentarioDAO;
import model.Comentario;
import java.time.LocalDateTime;

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

        try {
            usuarioDAO.criarUsuariosPadrao();

            boolean executando = true;

            while (executando) {
                System.out.println("\n=== CENTRAL DE SOLICITAÇÕES ===");
                System.out.println("1 - Fazer solicitação anônima");
                System.out.println("2 - Área do usuário comum");
                System.out.println("3 - Área do atendente");
                System.out.println("4 - Área do gestor");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");

                int op = Integer.parseInt(scanner.nextLine());

                switch (op) {
                    case 1:
                        Usuario anonimo = new Usuario(TipoUsuario.USUARIO_ANONIMO);
                        criarSolicitacao(scanner, solicitacaoDAO, anonimo);
                        break;

                    case 2:
                        menuUsuario(scanner, authService, solicitacaoDAO);
                        break;

                    case 3:
                        menuAtendente(scanner, authService, solicitacaoDAO);
                        break;

                    case 4:
                        menuGestor(scanner, authService, solicitacaoDAO);
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
            System.out.println("\n=== ÁREA DO USUÁRIO ===");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastrar usuário comum");
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

                    if (usuario == null || usuario.getTipoUsuario() != TipoUsuario.USUARIO_LOGADO) {
                        System.out.println("Login inválido para usuário comum.");
                        break;
                    }

                    boolean menuLogado = true;
                    while (menuLogado) {
                        System.out.println("\n=== USUÁRIO LOGADO ===");
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

                case 0:
                    voltar = true;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAtendente(Scanner scanner, AuthService authService, SolicitacaoDAO solicitacaoDAO) throws SQLException {
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
                        System.out.println("\n=== MENU ATENDENTE ===");
                        System.out.println("1 - Listar pendentes");
                        System.out.println("2 - Colocar em atendimento");
                        System.out.println("3 - Concluir solicitação");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        int acao = Integer.parseInt(scanner.nextLine());

                        switch (acao) {
                            case 1:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE));
                                break;
                            case 2:
                                listarSolicitacoes(solicitacaoDAO.listarPorStatus(StatusSolicitacao.PENDENTE));
                                System.out.print("Digite o ID da solicitação mostrado acima: ");
                                int idAtender = Integer.parseInt(scanner.nextLine());
                                solicitacaoDAO.atualizarStatus(idAtender, StatusSolicitacao.EM_ATENDIMENTO);
                                System.out.println("Solicitação em atendimento.");
                                break;
                            case 3:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("Digite o ID da solicitação mostrado acima: ");
                                int idConcluir = Integer.parseInt(scanner.nextLine());
                                solicitacaoDAO.atualizarStatus(idConcluir, StatusSolicitacao.CONCLUIDA);
                                System.out.println("Solicitação concluída.");
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

    private static void menuGestor(Scanner scanner, AuthService authService, SolicitacaoDAO solicitacaoDAO) throws SQLException {
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
                        System.out.println("\n=== MENU GESTOR ===");
                        System.out.println("1 - Listar todas");
                        System.out.println("2 - Colocar em atendimento");
                        System.out.println("3 - Concluir");
                        System.out.println("4 - Cancelar");
                        System.out.println("5 - Cadastrar atendente");
                        System.out.println("0 - Logout");
                        System.out.print("Escolha: ");

                        int acao = Integer.parseInt(scanner.nextLine());

                        switch (acao) {
                            case 1:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                break;
                            case 2:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("Digite o ID da solicitação mostrado acima: ");
                                int idAtender = Integer.parseInt(scanner.nextLine());
                                solicitacaoDAO.atualizarStatus(idAtender, StatusSolicitacao.EM_ATENDIMENTO);
                                System.out.println("Solicitação em atendimento.");
                                break;
                            case 3:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("Digite o ID da solicitação mostrado acima: ");
                                int idConcluir = Integer.parseInt(scanner.nextLine());
                                solicitacaoDAO.atualizarStatus(idConcluir, StatusSolicitacao.CONCLUIDA);
                                System.out.println("Solicitação concluída.");
                                break;
                            case 4:
                                listarSolicitacoes(solicitacaoDAO.listarSolicitacoes());
                                System.out.print("Digite o ID da solicitação mostrado acima: ");
                                int idCancelar = Integer.parseInt(scanner.nextLine());
                                solicitacaoDAO.atualizarStatus(idCancelar, StatusSolicitacao.CANCELADA);
                                System.out.println("Solicitação cancelada.");
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
            System.out.println("5 - DENUNCIA_ASSEDIO");
            System.out.println("6 - SOLICITACAO_ILUMINACAO");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            int opSolicitacao = Integer.parseInt(scanner.nextLine());

            if (opSolicitacao == 0) {
                return;
            }

            TipoSolicitacao tipoSolicitacao = null;

            switch (opSolicitacao) {
                case 1:
                    tipoSolicitacao = TipoSolicitacao.DENUNCIA_BURACO;
                    break;
                case 2:
                    tipoSolicitacao = TipoSolicitacao.SOLICITACAO_PODA;
                    break;
                case 3:
                    tipoSolicitacao = TipoSolicitacao.SOLICITACAO_ARVORE_CAIDA;
                    break;
                case 4:
                    tipoSolicitacao = TipoSolicitacao.SOLICITACAO_REFORMA;
                    break;
                case 5:
                    tipoSolicitacao = TipoSolicitacao.DENUNCIA_ASSEDIO;
                    break;
                case 6:
                    tipoSolicitacao = TipoSolicitacao.SOLICITACAO_ILUMINACAO;
                    break;
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

            Solicitacao solicitacao = new Solicitacao(
                    protocolo.getProtocolo(),
                    tipoSolicitacao,
                    Prioridade.ALTA,
                    descricao,
                    usuario.getUsuarioId()
            );

            solicitacaoDAO.criarSolicitacao(solicitacao);

            System.out.println("Solicitação criada com sucesso!");
            System.out.println("Protocolo: " + solicitacao.getSolicitacaoProtocolo());

            System.out.println("Deseja criar outra solicitação?");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");
            System.out.print("Escolha: ");

            int resposta = Integer.parseInt(scanner.nextLine());
            if (resposta != 1) {
                continuar = false;
            }
        }
    }

    private static void listarSolicitacoes(List<Solicitacao> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            return;
        }

        for (Solicitacao s : lista) {
            System.out.println("--------------------------------");
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