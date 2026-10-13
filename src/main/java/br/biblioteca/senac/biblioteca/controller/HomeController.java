package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.model.Autor;
import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HomeController {

    private LivroController livroController;
    private UsuarioController usuarioController;
    private EmprestimoController emprestimoController;
    private Scanner scanner;

    public HomeController() {
        this.livroController = new LivroController();
        this.usuarioController = new UsuarioController();
        this.emprestimoController = new EmprestimoController();
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuPrincipal() {
        int opcao = -1;

        do {
            System.out.println("\n========================================");
            System.out.println("      SISTEMA DE BIBLIOTECA - SENAC     ");
            System.out.println("========================================");
            System.out.println("1. Gerenciar Livros");
            System.out.println("2. Gerenciar Usuários");
            System.out.println("3. Gerenciar Empréstimos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (opcao) {
                    case 1:
                        menuLivros();
                        break;
                    case 2:
                        menuUsuarios();
                        break;
                    case 3:
                        menuEmprestimos();
                        break;
                    case 0:
                        System.out.println("\nEncerrando o sistema. Até logo!");
                        break;
                    default:
                        System.out.println("\n[Erro] Opção inválida! Escolha uma opção entre 0 e 3.");
                }
            } else {
                System.out.println("\n[Erro] Entrada inválida! Digite apenas números.");
                scanner.nextLine(); // Limpar entrada incorreta
            }

        } while (opcao != 0);
    }

    private void menuLivros() {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR LIVROS ---");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Listar Todos os Livros");
            System.out.println("3. Buscar Livro por ID");
            System.out.println("4. Buscar Livro por ISBN");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.print("Título: ");
                        String titulo = scanner.nextLine();
                        
                        System.out.print("ID do Autor: ");
                        Long autorId = scanner.nextLong();
                        scanner.nextLine();
                        System.out.print("Nome do Autor: ");
                        String autorNome = scanner.nextLine();
                        System.out.print("Nacionalidade do Autor: ");
                        String autorNacionalidade = scanner.nextLine();
                        Autor autor = new Autor(autorId, autorNome, autorNacionalidade);

                        System.out.print("Quantidade Total de Exemplares: ");
                        int qtdTotal = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("ISBN: ");
                        String isbn = scanner.nextLine();

                        System.out.print("Editora: ");
                        String editora = scanner.nextLine();

                        Livro livro = new Livro(titulo, autor, qtdTotal, isbn, editora);
                        livro.setQuantidadeDisponivel(qtdTotal); // Inicialmente igual ao total

                        livroController.cadastrarLivro(livro);
                        break;

                    case 2:
                        List<Livro> livros = livroController.listarTodos();
                        if (livros != null && !livros.isEmpty()) {
                            System.out.println("\n--- Lista de Livros ---");
                            for (Livro l : livros) {
                                System.out.println("ID: " + l.getId() + " | Título: " + l.getTitulo() + " | ISBN: " + l.getIsbn() + " | Disp: " + l.getQuantidadeDisponivel());
                            }
                        } else {
                            System.out.println("Nenhum livro cadastrado.");
                        }
                        break;

                    case 3:
                        System.out.print("Digite o ID do livro: ");
                        Long idLivro = scanner.nextLong();
                        scanner.nextLine();
                        Livro liv = livroController.buscarPorId(idLivro);
                        if (liv != null) {
                            System.out.println("Encontrado: " + liv.getTitulo() + " (Editora: " + liv.getEditora() + ")");
                        } else {
                            System.out.println("Livro não encontrado.");
                        }
                        break;

                    case 4:
                        System.out.print("Digite o ISBN do livro: ");
                        String isbnBusca = scanner.nextLine();
                        Livro livIsbn = livroController.buscarPorIsbn(isbnBusca);
                        if (livIsbn != null) {
                            System.out.println("Encontrado: " + livIsbn.getTitulo() + " (Editora: " + livIsbn.getEditora() + ")");
                        } else {
                            System.out.println("Livro não encontrado.");
                        }
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Digite um número válido.");
            }
        } while (opcao != 0);
    }

    private void menuUsuarios() {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR USUÁRIOS ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Todos os Usuários");
            System.out.println("3. Atualizar Usuário");
            System.out.println("4. Excluir Usuário");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("CPF: ");
                        String cpf = scanner.nextLine();
                        System.out.print("E-mail: ");
                        String email = scanner.nextLine();
                        System.out.print("Telefone: ");
                        String telefone = scanner.nextLine();

                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setCPF(cpf);
                        usuario.setEmail(email);
                        usuario.setTelefone(telefone);

                        usuarioController.cadastrarUsuario(usuario);
                        break;

                    case 2:
                        List<Usuario> usuarios = usuarioController.listarTodos();
                        if (usuarios != null && !usuarios.isEmpty()) {
                            System.out.println("\n--- Lista de Usuários ---");
                            for (Usuario u : usuarios) {
                                System.out.println("ID: " + u.getId() + " | Nome: " + u.getNome() + " | CPF: " + u.getCPF());
                            }
                        } else {
                            System.out.println("Nenhum usuário cadastrado.");
                        }
                        break;

                    case 3:
                        System.out.print("Digite o ID do usuário que deseja atualizar: ");
                        Long idUpdate = scanner.nextLong();
                        scanner.nextLine();
                        
                        System.out.print("Novo Nome: ");
                        String novoNome = scanner.nextLine();
                        System.out.print("Novo CPF: ");
                        String novoCpf = scanner.nextLine();
                        System.out.print("Novo E-mail: ");
                        String novoEmail = scanner.nextLine();
                        System.out.print("Novo Telefone: ");
                        String novoTelefone = scanner.nextLine();

                        Usuario userUpdate = new Usuario();
                        userUpdate.setId(idUpdate);
                        userUpdate.setNome(novoNome);
                        userUpdate.setCPF(novoCpf);
                        userUpdate.setEmail(novoEmail);
                        userUpdate.setTelefone(novoTelefone);

                        usuarioController.atualizarUsuario(userUpdate);
                        break;

                    case 4:
                        System.out.print("Digite o ID do usuário a ser excluído: ");
                        Long idDel = scanner.nextLong();
                        scanner.nextLine();
                        usuarioController.excluirUsuario(idDel);
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Digite um número válido.");
            }
        } while (opcao != 0);
    }

    private void menuEmprestimos() {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR EMPRÉSTIMOS ---");
            System.out.println("1. Realizar Empréstimo");
            System.out.println("2. Realizar Devolução");
            System.out.println("3. Listar Todos os Empréstimos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.print("ID do Usuário: ");
                        Long idUser = scanner.nextLong();
                        System.out.print("ID do Livro: ");
                        Long idLiv = scanner.nextLong();
                        scanner.nextLine();

                        Usuario u = new Usuario();
                        u.setId(idUser);

                        Livro l = new Livro();
                        l.setId(idLiv);

                        Emprestimo emp = new Emprestimo();
                        emp.setUsuario(u);
                        emp.setLivro(l);
                        emp.setDataEmprestimo(LocalDate.now());

                        emprestimoController.realizarEmprestimo(emp);
                        break;

                    case 2:
                        System.out.print("ID do Empréstimo para devolução: ");
                        Long idEmp = scanner.nextLong();
                        scanner.nextLine();
                        emprestimoController.realizarDevolucao(idEmp);
                        break;

                    case 3:
                        List<Emprestimo> emprestimos = emprestimoController.listarTodos();
                        if (emprestimos != null && !emprestimos.isEmpty()) {
                            System.out.println("\n--- Lista de Empréstimos ---");
                            for (Emprestimo e : emprestimos) {
                                System.out.println("ID Emp: " + e.getId() + " | Usuário ID: " + e.getUsuario().getId() + " | Livro ID: " + e.getLivro().getId() + " | Retirada: " + e.getDataEmprestimo());
                            }
                        } else {
                            System.out.println("Nenhum empréstimo registrado.");
                        }
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Digite um número válido.");
            }
        } while (opcao != 0);
    }
}