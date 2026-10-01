package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.dao.EmprestimoDao;
import br.biblioteca.senac.biblioteca.dao.LivroDao;
import br.biblioteca.senac.biblioteca.dao.UsuarioDao;
import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {

    private EmprestimoDao emprestimoDAO;
    private LivroDao livroDAO;
    private UsuarioDao usuarioDAO;

    public EmprestimoService() {
        this.emprestimoDAO = new EmprestimoDao();
        this.livroDAO = new LivroDao();
        this.usuarioDAO = new UsuarioDao();
    }

    public void realizarEmprestimo(Emprestimo emprestimo) throws SQLException, IllegalArgumentException {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Os dados do empréstimo não podem ser nulos.");
        }

        if (emprestimo.getLivro() == null || emprestimo.getLivro().getId() == null) {
            throw new IllegalArgumentException("O livro informado para o empréstimo é inválido.");
        }

        if (emprestimo.getUsuario() == null || emprestimo.getUsuario().getId() == null) {
            throw new IllegalArgumentException("O usuário informado para o empréstimo é inválido.");
        }

        Usuario usuario = usuarioDAO.buscarPorId(emprestimo.getUsuario().getId());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado no sistema.");
        }

        Livro livro = livroDAO.buscarPorId(emprestimo.getLivro().getId());
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado no sistema.");
        }

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalArgumentException("Não há exemplares disponíveis deste livro para empréstimo no momento.");
        }

        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        
        if (emprestimo.getDataDevolucaoPrevista() == null) {
            emprestimo.setDataDevolucaoPrevista(emprestimo.getDataEmprestimo().plusDays(14));
        }

        emprestimoDAO.inserir(emprestimo);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroDAO.atualizar(livro);
    }

    public void realizarDevolucao(Long emprestimoId) throws SQLException, IllegalArgumentException {
        if (emprestimoId == null || emprestimoId <= 0) {
            throw new IllegalArgumentException("ID do empréstimo inválido.");
        }

        Emprestimo emprestimo = emprestimoDAO.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado.");
        }

        if (emprestimo.getDataDevolucaoReal() != null) {
            throw new IllegalArgumentException("Este empréstimo já foi finalizado/devolvido.");
        }

        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimoDAO.atualizar(emprestimo);

        Livro livro = livroDAO.buscarPorId(emprestimo.getLivro().getId());
        if (livro != null) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            livroDAO.atualizar(livro);
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        return emprestimoDAO.listarTodos();
    }

    public Emprestimo buscarPorId(Long id) throws SQLException {
        return emprestimoDAO.buscarPorId(id);
    }
}