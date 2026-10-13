package br.biblioteca.senac.biblioteca.dao;

import java.util.List;

import br.biblioteca.senac.biblioteca.model.Livro;

public interface LivroDao {
    void atualizar(Livro livro);
    void deletar(Long id);
    Livro buscarPorId(Long id);      // <-- Adicione esta linha
    Livro buscarPorIsbn(String isbn);
    List<Livro> listarTodos();
}