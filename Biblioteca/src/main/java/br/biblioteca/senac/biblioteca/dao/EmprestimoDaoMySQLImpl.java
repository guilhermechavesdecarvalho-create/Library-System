package br.biblioteca.senac.biblioteca.dao;

import java.sql.*;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.Emprestimo;

public class EmprestimoDaoMySQLImpl implements EmprestimoDao {

    @Override
    public void salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (usuario_id, livro_id, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, emprestimo.getUsuario().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));

            stmt.executeUpdate();

            // Opcional: recuperar o ID gerado pelo banco
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getLong(1));
                }
            }

            System.out.println("Empréstimo salvo com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o empréstimo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}