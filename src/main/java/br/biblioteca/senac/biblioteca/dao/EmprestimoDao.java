package br.biblioteca.senac.biblioteca.dao;

import java.util.List;
import br.biblioteca.senac.biblioteca.model.Emprestimo;

public interface EmprestimoDao {
    void salvar(Emprestimo emprestimo);
    void atualizar(Emprestimo emprestimo);
    Emprestimo buscarPorId(Long id);
    List<Emprestimo> listarTodos();
}
