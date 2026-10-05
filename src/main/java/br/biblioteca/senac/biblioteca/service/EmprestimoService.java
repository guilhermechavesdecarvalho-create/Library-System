package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.dao.EmprestimoDao;
import br.biblioteca.senac.biblioteca.dao.EmprestimoDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.dao.LivroDao;
import br.biblioteca.senac.biblioteca.dao.LivroDaoMySQLImpl; // Ajuste se o nome da implementação do seu LivroDao for diferente
import br.biblioteca.senac.biblioteca.dao.UsuarioDao;
import br.biblioteca.senac.biblioteca.dao.UsuarioDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {

    private EmprestimoDao emprestimoDao;
    private LivroDao livroDao;
    private UsuarioDao usuarioDao;

    public EmprestimoService() {
        // Corrigido para instanciar as implementações corretas
        this.emprestimoDao = new EmprestimoDaoMySQLImpl();
        this.livroDao = new LivroDaoMySQLImpl(); 
        this.usuarioDao = new UsuarioDaoMySQLImpl();
    }

    public void realizarEmprestimo(Emprestimo emprestimo) throws SQLException, IllegalArgumentException {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Os dados do empréstimo não podem ser nulos.");
        }

        if (emprestimo.getLivro() == null || emprestimo.getLivro().getId() <= 0) {
            throw new IllegalArgumentException("O livro informado para o empréstimo é inválido.");
        }

        if (emprestimo.getUsuario() == null || emprestimo.getUsuario().getId() <= 0) {
            throw new IllegalArgumentException("O usuário informado para o empréstimo é inválido.");
        }

        Usuario usuario = usuarioDao.buscarPorId(emprestimo.getUsuario().getId());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado no sistema.");
        }

        Livro livro = livroDao.buscarPorId(emprestimo.getLivro().getId());
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado no sistema.");
        }

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalArgumentException("Não há exemplares disponíveis deste livro para empréstimo no momento.");
        }

        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        
        if (emprestimo.getDataDevolucao() == null) {
            emprestimo.setDataDevolucao(emprestimo.getDataEmprestimo().plusDays(14));
        }

        // Corrigido de inserir para salvar
        emprestimoDao.salvar(emprestimo);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroDao.atualizar(livro);
    }

    public void realizarDevolucao(Long emprestimoId) throws SQLException, IllegalArgumentException {
        if (emprestimoId == null || emprestimoId <= 0) {
            throw new IllegalArgumentException("ID do empréstimo inválido.");
        }

        Emprestimo emprestimo = emprestimoDao.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado.");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        
        // Corrigido o erro de digitação da variável (emprestimoDAO -> emprestimoDao)
        emprestimoDao.atualizar(emprestimo);

        Livro livro = livroDao.buscarPorId(emprestimo.getLivro().getId());
        if (livro != null) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            livroDao.atualizar(livro);
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        return emprestimoDao.listarTodos();
    }

    public Emprestimo buscarPorId(Long id) throws SQLException {
        return emprestimoDao.buscarPorId(id);
    }
}