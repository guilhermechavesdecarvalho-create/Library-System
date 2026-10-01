package br.biblioteca.senac.biblioteca.dao;

import java.util.List;

import br.biblioteca.senac.biblioteca.model.Livro;

public interface LivroDao{
    void salvar(Livro livro);
    Livro buscarPorIsbn(String isbn);
    List<Livro> listarTodos();
}
