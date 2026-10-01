package br.biblioteca.senac.biblioteca.dao;

import java.util.List;
import br.biblioteca.senac.biblioteca.model.Usuario;

public interface UsuarioDao {
    void salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
    
}
