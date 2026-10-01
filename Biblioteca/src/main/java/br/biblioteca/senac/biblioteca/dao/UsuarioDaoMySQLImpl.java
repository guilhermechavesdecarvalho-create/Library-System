package br.biblioteca.senac.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.Usuario;

public class UsuarioDaoMySQLImpl implements UsuarioDao {

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, email, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCPF());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefone());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

            System.out.println("Usuário salvo com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o usuário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCPF(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefone(rs.getString("telefone"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCPF(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTelefone(rs.getString("telefone"));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar os usuários: " + e.getMessage());
        }
        return usuarios;
    }
}