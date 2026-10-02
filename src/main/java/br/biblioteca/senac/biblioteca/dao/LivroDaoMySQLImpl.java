package br.biblioteca.senac.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.Autor;
import br.biblioteca.senac.biblioteca.model.Livro;


public class LivroDaoMySQLImpl implements LivroDao {

    @Override
    public void atualizar(Livro livro) {
        String sql = "INSERT INTO livro ( id, titulo, quantidade_total, quantidade_disponivel, isbn, editora, autor_id) VALUES (?,?,?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, livro.getId());
            stmt.setString(2, livro.getTitulo());
            stmt.setInt(3, livro.getQuantidadeTotal());
            stmt.setInt(4, livro.getQuantidadeDisponivel());
            stmt.setString(5, livro.getIsbn());
            stmt.setString(6, livro.getEditora());
            stmt.setLong(7, livro.getAutor().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getLong(1));
                }
            }

            System.out.println("Livro salvo com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o livro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        String sql = "SELECT l.*, a.nome as autor_nome, a.nacionalidade as autor_nacionalidade " +
                     "FROM livro l INNER JOIN autor a ON l.autor_id = a.id WHERE l.isbn = ?";
        
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor(
                        rs.getLong("autor_id"),
                        rs.getString("autor_nome"),
                        rs.getString("autor_nacionalidade")
                    );

                    Livro livro = new Livro(
                        rs.getString("titulo"),
                        autor,
                        rs.getInt("quantidade_total"),
                        rs.getString("ISBN"),
                        rs.getString("editora")
                    );

                    livro.setId(rs.getLong("id"));
                    livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));

                    return livro;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ISBN: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.*, a.nome as autor_nome, a.nacionalidade as autor_nacionalidade " +
                     "FROM livro l INNER JOIN autor a ON l.autor_id = a.id";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getLong("autor_id"),
                    rs.getString("autor_nome"),
                    rs.getString("autor_nacionalidade")
                );

                Livro livro = new Livro(
                    rs.getString("titulo"),
                    autor,
                    rs.getInt("quantidade_total"),
                    rs.getString("ISBN"),
                    rs.getString("editora")
                );
                
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar os livros: " + e.getMessage());
        }
        return livros;
    }

    @Override
    public Livro buscarPorId(Long id) {
        String sql = "SELECT l.*, a.nome as autor_nome, a.nacionalidade as autor_nacionalidade " +
                     "FROM livro l INNER JOIN autor a ON l.autor_id = a.id WHERE l.id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor(
                        rs.getLong("autor_id"),
                        rs.getString("autor_nome"),
                        rs.getString("autor_nacionalidade")
                    );

                    Livro livro = new Livro(
                        rs.getString("titulo"),
                        autor,
                        rs.getInt("quantidade_total"),
                        rs.getString("ISBN"),
                        rs.getString("editora")
                    );

                    livro.setId(rs.getLong("id"));
                    livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));

                    return livro;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }
        return null;
    }
}