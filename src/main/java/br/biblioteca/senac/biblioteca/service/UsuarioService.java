package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.dao.UsuarioDao;
import br.biblioteca.senac.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private UsuarioDao usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDao();
    }

    public void cadastrarUsuario(Usuario usuario) throws SQLException, IllegalArgumentException {
        validarUsuario(usuario);

        if (usuarioDAO.buscarPorCpf(usuario.getCpf()) != null) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este CPF.");
        }

        usuarioDAO.inserir(usuario);
    }

    public void atualizarUsuario(Usuario usuario) throws SQLException, IllegalArgumentException {
        if (usuario.getId() == null || usuario.getId() <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido para atualização.");
        }
        validarUsuario(usuario);
        usuarioDAO.atualizar(usuario);
    }

    public void excluirUsuario(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        usuarioDAO.deletar(id);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        return usuarioDAO.buscarPorId(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Os dados do usuário não podem ser nulos.");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do usuário é obrigatório.");
        }
        if (usuario.getCpf().replaceAll("\\D", "").length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        }
    }
}
