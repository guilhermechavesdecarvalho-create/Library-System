package br.biblioteca.senac.biblioteca.service;

import br.biblioteca.senac.biblioteca.connection.Conexao;
import br.biblioteca.senac.biblioteca.dao.EmprestimoDao;
import br.biblioteca.senac.biblioteca.dao.EmprestimoDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.dao.LivroDao;
import br.biblioteca.senac.biblioteca.dao.LivroDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.dao.UsuarioDao;
import br.biblioteca.senac.biblioteca.dao.UsuarioDaoMySQLImpl;
import br.biblioteca.senac.biblioteca.model.Emprestimo;
import br.biblioteca.senac.biblioteca.model.Livro;
import br.biblioteca.senac.biblioteca.model.Usuario;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/** Regras de empréstimo com atualização transacional de registro e estoque. */
public class EmprestimoService {
    private final EmprestimoDao emprestimoDao = new EmprestimoDaoMySQLImpl();
    private final LivroDao livroDao = new LivroDaoMySQLImpl();
    private final UsuarioDao usuarioDao = new UsuarioDaoMySQLImpl();

    public void realizarEmprestimo(Emprestimo e) throws SQLException {
        if (e == null || e.getLivro() == null || e.getUsuario() == null || e.getLivro().getId() <= 0
                || e.getUsuario().getId() <= 0)
            throw new IllegalArgumentException("Selecione um livro e um usuário válidos.");
        Usuario u = usuarioDao.buscarPorId(e.getUsuario().getId());
        if (u == null)
            throw new IllegalArgumentException("Usuário não encontrado.");
        Livro l = livroDao.buscarPorId(e.getLivro().getId());
        if (l == null)
            throw new IllegalArgumentException("Livro não encontrado.");

        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);
            try {
                // Bloqueia o registro do livro para impedir dois empréstimos do último exemplar
                // simultaneamente.
                int disponiveis;
                try (PreparedStatement p = c
                        .prepareStatement("SELECT quantidade_disponivel FROM livro WHERE id=? FOR UPDATE")) {
                    p.setLong(1, l.getId());
                    try (ResultSet r = p.executeQuery()) {
                        if (!r.next())
                            throw new IllegalArgumentException("Livro não encontrado.");
                        disponiveis = r.getInt(1);
                    }
                }
                if (disponiveis < 1)
                    throw new IllegalArgumentException("Não há exemplares disponíveis deste livro.");
                // Serializa empréstimos do mesmo usuário para respeitar o limite mesmo em
                // requisições concorrentes.
                try (PreparedStatement p = c.prepareStatement("SELECT id FROM usuario WHERE id=? FOR UPDATE")) {
                    p.setLong(1, u.getId());
                    try (ResultSet r = p.executeQuery()) {
                        if (!r.next())
                            throw new IllegalArgumentException("Usuário não encontrado.");
                    }
                }
                try (PreparedStatement p = c
                        .prepareStatement("SELECT COUNT(*) FROM emprestimo WHERE usuario_id=? AND status=TRUE")) {
                    p.setLong(1, u.getId());
                    try (ResultSet r = p.executeQuery()) {
                        r.next();
                        if (r.getInt(1) >= 3)
                            throw new IllegalArgumentException(
                                    "O usuário já atingiu o limite de 3 empréstimos ativos.");
                    }
                }
                LocalDate data = e.getDataEmprestimo() == null ? LocalDate.now() : e.getDataEmprestimo();
                LocalDate prazo = data.plusDays(14);
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO emprestimo(livro_id,usuario_id,data_emprestimo,data_evolucao,status) VALUES(?,?,?,?,TRUE)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    p.setLong(1, l.getId());
                    p.setLong(2, u.getId());
                    p.setDate(3, Date.valueOf(data));
                    p.setDate(4, Date.valueOf(prazo));
                    p.executeUpdate();
                    try (ResultSet r = p.getGeneratedKeys()) {
                        if (r.next())
                            e.setId(r.getLong(1));
                    }
                }
                try (PreparedStatement p = c.prepareStatement(
                        "UPDATE livro SET quantidade_disponivel=quantidade_disponivel-1 WHERE id=? AND quantidade_disponivel>0")) {
                    p.setLong(1, l.getId());
                    if (p.executeUpdate() != 1)
                        throw new IllegalArgumentException(
                                "O último exemplar acabou de ser emprestado. Atualize a página e tente novamente.");
                }
                c.commit();
                e.setLivro(l);
                e.setUsuario(u);
                e.setDataEmprestimo(data);
                e.setDataDevolucao(prazo);
                e.setStatus(true);
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException sql)
                    throw sql;
                if (ex instanceof RuntimeException runtime)
                    throw runtime;
                throw new SQLException("Falha ao registrar empréstimo.", ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void realizarDevolucao(Long id) throws SQLException {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID do empréstimo inválido.");
        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);
            try {
                long livroId;
                try (PreparedStatement p = c
                        .prepareStatement("SELECT livro_id,status FROM emprestimo WHERE id=? FOR UPDATE")) {
                    p.setLong(1, id);
                    try (ResultSet r = p.executeQuery()) {
                        if (!r.next())
                            throw new IllegalArgumentException("Empréstimo não encontrado.");
                        if (!r.getBoolean("status"))
                            throw new IllegalArgumentException("Este empréstimo já foi devolvido.");
                        livroId = r.getLong("livro_id");
                    }
                }
                try (PreparedStatement p = c
                        .prepareStatement("UPDATE emprestimo SET status=FALSE WHERE id=? AND status=TRUE")) {
                    p.setLong(1, id);
                    if (p.executeUpdate() != 1)
                        throw new IllegalArgumentException("Este empréstimo já foi devolvido.");
                }
                try (PreparedStatement p = c.prepareStatement(
                        "UPDATE livro SET quantidade_disponivel=LEAST(quantidade_total,quantidade_disponivel+1) WHERE id=?")) {
                    p.setLong(1, livroId);
                    if (p.executeUpdate() != 1)
                        throw new IllegalStateException("Livro relacionado ao empréstimo não foi encontrado.");
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException sql)
                    throw sql;
                if (ex instanceof RuntimeException runtime)
                    throw runtime;
                throw new SQLException("Falha ao registrar devolução.", ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        return emprestimoDao.listarTodos();
    }

    public Emprestimo buscarPorId(Long id) throws SQLException {
        return emprestimoDao.buscarPorId(id);
    }
}
