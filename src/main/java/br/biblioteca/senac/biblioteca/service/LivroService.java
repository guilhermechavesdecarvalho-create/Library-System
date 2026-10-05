package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.dao.LivroDao;
import br.biblioteca.senac.biblioteca.model.Livro;

import java.util.List;

public class LivroService {

    private LivroDao livroDao;

    public LivroService(LivroDao livroDao) {
        this.livroDao = livroDao;
    }
    
    public void cadastrarLivro(Livro livro) {
        validarLivro(livro);

        if (livroDao.buscarPorIsbn(livro.getIsbn()) != null) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com este ISBN.");
        }

        livroDao.atualizar(livro);
    }

    public List<Livro> listarTodos() {
        return livroDao.listarTodos();
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