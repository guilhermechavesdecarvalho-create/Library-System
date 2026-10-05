package br.biblioteca.senac.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.model.Usuario;

public class EmprestimoDaoMySQLImpl implements EmprestimoDao {

    @Override
    public void salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (usuario_id, livro_id, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, emprestimo.getUsuario().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            
            // Usando getDataDevolucao() conforme a model padrão
            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o empréstimo: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimo SET usuario_id = ?, livro_id = ?, data_emprestimo = ?, data_devolucao = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, emprestimo.getUsuario().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            
            // Usando getDataDevolucao() conforme a model padrão
            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setLong(5, emprestimo.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o empréstimo: " + e.getMessage(), e);
        }
    }

    @Override
    public Emprestimo buscarPorId(Long id) {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setId(rs.getLong("id"));
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("usuario_id"));
                    emprestimo.setUsuario(usuario);

                    Livro livro = new Livro();
                    livro.setId(rs.getLong("livro_id"));
                    emprestimo.setLivro(livro);

                    if (rs.getDate("data_emprestimo") != null) {
                        emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                    }
                    
                    // Usando setDataDevolucao() conforme a model padrão
                    if (rs.getDate("data_devolucao") != null) {
                        emprestimo.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());
                    }

                    return emprestimo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimo por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getLong("id"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                emprestimo.setUsuario(usuario);

                Livro livro = new Livro();
                livro.setId(rs.getLong("livro_id"));
                emprestimo.setLivro(livro);

                if (rs.getDate("data_emprestimo") != null) {
                    emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                }
                
                // Usando setDataDevolucao() conforme a model padrão
                if (rs.getDate("data_devolucao") != null) {
                    emprestimo.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());
                }

                lista.add(emprestimo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos: " + e.getMessage(), e);
        }
        return lista;
    }
}