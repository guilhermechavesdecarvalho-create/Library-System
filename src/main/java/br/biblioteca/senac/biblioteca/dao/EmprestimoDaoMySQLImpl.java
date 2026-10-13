package br.biblioteca.senac.biblioteca.dao;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDaoMySQLImpl implements EmprestimoDao {
    private static final String SELECT = "SELECT e.*, u.nome AS usuario_nome, u.cpf AS usuario_cpf, u.email AS usuario_email, u.telefone AS usuario_telefone, l.titulo AS livro_titulo, l.isbn AS livro_isbn, l.editora AS livro_editora, l.quantidade_total, l.quantidade_disponivel, l.autor_id, a.nome AS autor_nome, a.nacionalidade AS autor_nacionalidade FROM emprestimo e JOIN usuario u ON u.id=e.usuario_id JOIN livro l ON l.id=e.livro_id JOIN autor a ON a.id=l.autor_id ";

    @Override
    public void salvar(Emprestimo e) {
        String sql = "INSERT INTO emprestimo(usuario_id,livro_id,data_emprestimo,data_evolucao,status) VALUES(?,?,?,?,?)";
        try (Connection c = Conexao.conectar();
                PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setLong(1, e.getUsuario().getId());
            p.setLong(2, e.getLivro().getId());
            p.setDate(3, Date.valueOf(e.getDataEmprestimo()));
            p.setDate(4, Date.valueOf(e.getDataDevolucao()));
            p.setBoolean(5, e.isStatus());
            p.executeUpdate();
            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next())
                    e.setId(r.getLong(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao registrar empréstimo.", ex);
        }
    }

    @Override
    public void atualizar(Emprestimo e) {
        try (Connection c = Conexao.conectar();
                PreparedStatement p = c.prepareStatement("UPDATE emprestimo SET status=? WHERE id=?")) {
            p.setBoolean(1, e.isStatus());
            p.setLong(2, e.getId());
            if (p.executeUpdate() == 0)
                throw new IllegalArgumentException("Empréstimo não encontrado.");
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar empréstimo.", ex);
        }
    }

    @Override
    public Emprestimo buscarPorId(Long id) {
        return consultar(SELECT + "WHERE e.id=?", id);
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> out = new ArrayList<>();
        try (Connection c = Conexao.conectar();
                PreparedStatement p = c.prepareStatement(SELECT + "ORDER BY e.status DESC,e.data_emprestimo DESC");
                ResultSet r = p.executeQuery()) {
            while (r.next())
                out.add(mapear(r));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos.", e);
        }
        return out;
    }

    private Emprestimo consultar(String sql, Long id) {
        try (Connection c = Conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? mapear(r) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar empréstimo.", e);
        }
    }

    private Emprestimo mapear(ResultSet r) throws SQLException {
        Usuario u = new Usuario();
        u.setId(r.getLong("usuario_id"));
        u.setNome(r.getString("usuario_nome"));
        u.setCPF(r.getString("usuario_cpf"));
        u.setEmail(r.getString("usuario_email"));
        u.setTelefone(r.getString("usuario_telefone"));
        Autor a = new Autor(r.getLong("autor_id"), r.getString("autor_nome"), r.getString("autor_nacionalidade"));
        Livro l = new Livro(r.getString("livro_titulo"), a, r.getInt("quantidade_total"), r.getString("livro_isbn"),
                r.getString("livro_editora"));
        l.setId(r.getLong("livro_id"));
        l.setQuantidadeDisponivel(r.getInt("quantidade_disponivel"));
        Emprestimo e = new Emprestimo();
        e.setId(r.getLong("id"));
        e.setUsuario(u);
        e.setLivro(l);
        e.setDataEmprestimo(r.getDate("data_emprestimo").toLocalDate());
        e.setDataDevolucao(r.getDate("data_evolucao").toLocalDate());
        e.setStatus(r.getBoolean("status"));
        return e;
    }
}
