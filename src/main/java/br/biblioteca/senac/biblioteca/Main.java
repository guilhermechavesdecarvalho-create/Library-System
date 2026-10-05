package br.biblioteca.senac.biblioteca;

import br.biblioteca.senac.biblioteca.dao.UsuarioDao;
import br.biblioteca.senac.biblioteca.dao.UsuarioDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.model.Usuario;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   INICIANDO TESTES DO SISTEMA DE BIBLIOTECA SENAC");
        System.out.println("==================================================\n");

        // Instancia o DAO (implementação MySQL)
        UsuarioDao usuarioDao = new UsuarioDaoMySQLImpl();

        // --- 1. TESTANDO AS VALIDAÇÕES DO MODEL ---
        System.out.println(">>> 1. Testando Validações do Model (CPF Inválido)");
        try {
            // Tentando cadastrar com um CPF inválido (menos de 11 dígitos)
            new Usuario("Lucas Silva", "1234", "Lucas@email.com", "55999999998");
        } catch (IllegalArgumentException e) {
            System.out.println("[SUCESSO NA VALIDAÇÃO]: " + e.getMessage() + "\n");
        }

        // --- 2. CRIANDO UM USUÁRIO VÁLIDO ---
        System.out.println(">>> 2. Criando Usuário Válido");
        Usuario novoUsuario = null;
        try {
            novoUsuario = new Usuario("Guilherme", "12345678921", "guilherme@email.com", "55988898888");
            System.out.println("Usuário instanciado com sucesso na memória!\n");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao instanciar: " + e.getMessage());
        }

        if (novoUsuario != null) {
            // --- 3. SALVANDO NO BANCO DE DADOS (DAO) ---
            System.out.println(">>> 3. Salvando no Banco de Dados MySQL");
            usuarioDao.salvar(novoUsuario);
            System.out.println("ID gerado automaticamente pelo banco: " + novoUsuario.getId() + "\n");

            // --- 4. TESTANDO AS REGRAS DE NEGÓCIO DE LIVROS ---
            System.out.println(">>> 4. Testando Empréstimos de Livros");
            novoUsuario.exibirStatus();

            // Pegando emprestado 3 livros (limite máximo permitido)
            novoUsuario.adicionarLivroEmprestado();
            novoUsuario.adicionarLivroEmprestado();
            novoUsuario.adicionarLivroEmprestado();

            // Tentando pegar o 4º livro (deve bloquear)
            System.out.println("Tentativa de pegar o 4º livro:");
            novoUsuario.adicionarLivroEmprestado(); 

            System.out.println();
            novoUsuario.exibirStatus();

            // --- 5. BUSCANDO O USUÁRIO POR ID NO BANCO ---
            System.out.println(">>> 5. Buscando Usuário por ID no Banco");
            Usuario usuarioBuscado = usuarioDao.buscarPorId(novoUsuario.getId());
            
            if (usuarioBuscado != null) {
                System.out.println("Usuário encontrado com sucesso!");
                System.out.println("Nome: " + usuarioBuscado.getNome() + " | E-mail: " + usuarioBuscado.getEmail() + " | Telefone: " + usuarioBuscado.getTelefone() + "\n");
            } else {
                System.out.println("Usuário não encontrado.\n");
            }

            // --- 6. LISTANDO TODOS OS USUÁRIOS ---
            System.out.println(">>> 6. Listando Todos os Usuários do Sistema");
            List<Usuario> listaUsuarios = usuarioDao.listarTodos();
            
            System.out.println("Total de usuários cadastrados: " + listaUsuarios.size());
            for (Usuario u : listaUsuarios) {
                System.out.println("- [ID: " + u.getId() + "] Nome: " + u.getNome() + " | CPF: " + u.getCPF());
            }
        }

        System.out.println("\n==================================================");
        System.out.println("   TESTES FINALIZADOS COM SUCESSO!");
        System.out.println("==================================================");
    }
}