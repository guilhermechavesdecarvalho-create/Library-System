package br.biblioteca.senac.biblioteca.dao;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.Autor;
import br.biblioteca.senac.biblioteca.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDaoMySQLImpl implements LivroDao {
    private static final String SELECT = "SELECT l.*, a.nome AS autor_nome, a.nacionalidade AS autor_nacionalidade FROM livro l JOIN autor a ON a.id=l.autor_id ";

    @Override
    public void atualizar(Livro livro) {
        if (livro.getAutor() == null)
            throw new IllegalArgumentException("Informe o autor do livro.");
        try (Connection c = Conexao.conectar()) {
            long autorId = garantirAutor(c, livro.getAutor());
            if (livro.getId() > 0) {
                try (PreparedStatement p = c.prepareStatement(
                        "UPDATE livro SET titulo=?, quantidade_total=?, quantidade_disponivel=?, isbn=?, editora=?, autor_id=? WHERE id=?")) {
                    preencher(p, livro, autorId, false);
                    p.setLong(7, livro.getId());
                    if (p.executeUpdate() == 0)
                        throw new IllegalArgumentException("Livro não encontrado para atualização.");
                }
            } else {
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO livro (titulo, quantidade_total, quantidade_disponivel, isbn, editora, autor_id) VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    preencher(p, livro, autorId, true);
                    p.executeUpdate();
                    try (ResultSet rs = p.getGeneratedKeys()) {
                        if (rs.next())
                            livro.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível salvar o livro. Verifique os dados e a conexão com o banco.",
                    e);
        }
    }

    private void preencher(PreparedStatement p, Livro l, long autorId, boolean insert) throws SQLException {
        p.setString(1, l.getTitulo());
        p.setInt(2, l.getQuantidadeTotal());
        p.setInt(3, l.getQuantidadeDisponivel());
        p.setString(4, l.getIsbn());
        p.setString(5, l.getEditora());
        p.setLong(6, autorId);
    }

    private long garantirAutor(Connection c, Autor autor) throws SQLException {
        if (autor.getId() > 0) {
            try (PreparedStatement p = c.prepareStatement("SELECT id FROM autor WHERE id=?")) {
                p.setLong(1, autor.getId());
                try (ResultSet r = p.executeQuery()) {
                    if (r.next())
                        return r.getLong(1);
                }
            }
        }
        try (PreparedStatement p = c.prepareStatement("SELECT id FROM autor WHERE nome=? AND nacionalidade=?")) {
            p.setString(1, autor.getNome());
            p.setString(2, autor.getNacionalidade());
            try (ResultSet r = p.executeQuery()) {
                if (r.next())
                    return r.getLong(1);
            }
        }
        try (PreparedStatement p = c.prepareStatement("INSERT INTO autor(nome,nacionalidade) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, autor.getNome());
            p.setString(2, autor.getNacionalidade());
            p.executeUpdate();
            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next())
                    return r.getLong(1);
            }
        }
        throw new SQLException("Não foi possível registrar o autor.");
    }

    @Override
    public void deletar(Long id) {
        try (Connection c = Conexao.conectar();
                PreparedStatement p = c.prepareStatement("DELETE FROM livro WHERE id=?")) {
            p.setLong(1, id);
            if (p.executeUpdate() == 0)
                throw new IllegalArgumentException("Livro não encontrado.");
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Não foi possível excluir o livro. Verifique se ele possui empréstimos registrados.", e);
        }
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        return consultarUm(SELECT + "WHERE l.isbn=?", isbn);
    }

    @Override
    public Livro buscarPorId(Long id) {
        return consultarUm(SELECT + "WHERE l.id=?", id);
    }

    private Livro consultarUm(String sql, Object valor) {
        try (Connection c = Conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            if (valor instanceof Long)
                p.setLong(1, (Long) valor);
            else
                p.setString(1, (String) valor);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? mapear(r) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar livro.", e);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        try (Connection c = Conexao.conectar();
                PreparedStatement p = c.prepareStatement(SELECT + "ORDER BY l.titulo");
                ResultSet r = p.executeQuery()) {
            while (r.next())
                lista.add(mapear(r));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros.", e);
        }
        return lista;
    }

    private Livro mapear(ResultSet r) throws SQLException {
        Autor a = new Autor(r.getLong("autor_id"), r.getString("autor_nome"), r.getString("autor_nacionalidade"));
        Livro l = new Livro(r.getString("titulo"), a, r.getInt("quantidade_total"), r.getString("isbn"),
                r.getString("editora"));
        l.setId(r.getLong("id"));
        l.setQuantidadeDisponivel(r.getInt("quantidade_disponivel"));
        return l;
    }
}
