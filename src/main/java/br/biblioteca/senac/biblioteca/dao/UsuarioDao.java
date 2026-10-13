package br.biblioteca.senac.biblioteca.dao;

import java.util.List;
import br.biblioteca.senac.biblioteca.model.Usuario;

public interface UsuarioDao {
    void salvar(Usuario usuario);
    void atualizar(Usuario usuario);
    void deletar(Long id);
    Usuario buscarPorId(Long id);
    Usuario buscarPorCpf(String cpf);
    Usuario buscarPorEmail(String email); // Adicionado para o Login
    List<Usuario> listarTodos();
}