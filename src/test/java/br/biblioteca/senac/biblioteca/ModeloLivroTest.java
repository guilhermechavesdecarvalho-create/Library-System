package br.biblioteca.senac.biblioteca;

import br.biblioteca.senac.biblioteca.model.Autor;
import br.biblioteca.senac.biblioteca.model.Livro;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModeloLivroTest {
    @Test void livroNovoComecaComTodosOsExemplaresDisponiveis() {
        Livro livro = new Livro("Java básico", new Autor(0, "Ana Silva", "Brasileira"), 4, "9780000000001", "Editora Senac");
        assertEquals(4, livro.getQuantidadeTotal());
        assertEquals(4, livro.getQuantidadeDisponivel());
    }
    @Test void livroNaoAceitaTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Livro(" ", new Autor(0, "Ana Silva", "Brasileira"), 1, "9780000000001", "Editora Senac"));
    }
}
