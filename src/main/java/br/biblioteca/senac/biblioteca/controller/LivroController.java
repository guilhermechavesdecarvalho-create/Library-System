package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.dao.LivroDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.service.LivroService;

import java.util.List;

public class LivroController {

    private LivroService livroService;

    public LivroController() {
        // Instancia o service passando a implementação do DAO do MySQL
        this.livroService = new LivroService(new LivroDaoMySQLImpl());
    }

    public void cadastrarLivro(Livro livro) {
        try {
            livroService.cadastrarLivro(livro);
            System.out.println("Sucesso: Livro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar o livro: " + e.getMessage());
        }
    }

    public List<Livro> listarTodos() {
        try {
            return livroService.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar os livros: " + e.getMessage());
            return null;
        }
    }

    public void excluir(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("ID de livro inválido.");
        livroService.excluir(id);
    }

    public Livro buscarPorId(Long id) {
        try {
            return livroService.buscarPorId(id);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao buscar o livro por ID: " + e.getMessage());
            return null;
        }
    }

    public Livro buscarPorIsbn(String isbn) {
        try {
            return livroService.buscarPorIsbn(isbn);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao buscar o livro por ISBN: " + e.getMessage());
            return null;
        }
    }
}