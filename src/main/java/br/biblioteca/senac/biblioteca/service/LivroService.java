package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.dao.LivroDao;
import br.biblioteca.senac.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class LivroService {

    private LivroDao livroDao;

    public LivroService() {
        this.livroDao = new livroDao();
    }

    public void cadastrarLivro(Livro livro) throws SQLException, IllegalArgumentException {
        validarLivro(livro);

        if (livroDao.buscarPorIsbn(livro.getIsbn()) != null) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com este ISBN.");
        }

        livroDao.inserir(livro);
    }

    public void atualizarLivro(Livro livro) throws SQLException, IllegalArgumentException {
        if (livro.getId() == null || livro.getId() <= 0) {
            throw new IllegalArgumentException("ID do livro inválido para atualização.");
        }
        validarLivro(livro);
        livroDao.atualizar(livro);
    }

    public void excluirLivro(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        livroDao.deletar(id);
    }

    public List<Livro> listarTodos() throws SQLException {
        return livroDao.listarTodos();
    }

    public Livro buscarPorId(Long id) throws SQLException {
        return livroDao.buscarPorId(id);
    }

    private void validarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Os dados do livro não podem ser nulos.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }
        if (livro.getIsbn() == null || livro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("O ISBN do livro é obrigatório.");
        }
        if (livro.getQuantidadeTotal() < 0) {
            throw new IllegalArgumentException("A quantidade de exemplares não pode ser negativa.");
        }
    }
}