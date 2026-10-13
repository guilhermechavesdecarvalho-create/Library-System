package br.biblioteca.senac.biblioteca.controller;

import br.biblioteca.senac.biblioteca.model.Usuario;
import br.biblioteca.senac.biblioteca.service.UsuarioService;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public void cadastrarUsuario(Usuario usuario) {
        try {
            usuarioService.cadastrarUsuario(usuario);
            System.out.println("Sucesso: Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados ao cadastrar usuário: " + e.getMessage());
        }
    }

    public void atualizarUsuario(Usuario usuario) {
        try {
            usuarioService.atualizarUsuario(usuario);
            System.out.println("Sucesso: Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados ao atualizar usuário: " + e.getMessage());
        }
    }

    public void excluirUsuario(Long id) {
        try {
            usuarioService.excluirUsuario(id);
            System.out.println("Sucesso: Usuário excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados ao excluir usuário: " + e.getMessage());
        }
    }

    public List<Usuario> listarTodos() {
        try {
            return usuarioService.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar os usuários: " + e.getMessage());
            return null;
        }
    }

    public Usuario buscarPorId(Long id) {
        try {
            return usuarioService.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar o usuário por ID: " + e.getMessage());
            return null;
        }
    }
}