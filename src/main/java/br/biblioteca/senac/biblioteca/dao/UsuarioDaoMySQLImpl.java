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
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCPF());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefone());
            stmt.setLong(5, usuario.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o usuário: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cpf);
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
            throw new RuntimeException("Erro ao buscar usuário por CPF: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao listar os usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCPF(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefone(rs.getString("telefone"));
                    // Se você tiver o campo senha na tabela/modelo, inclua aqui:
                    // usuario.setSenha(rs.getString("senha"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por E-mail: " + e.getMessage(), e);
        }
        return null;
    }
}