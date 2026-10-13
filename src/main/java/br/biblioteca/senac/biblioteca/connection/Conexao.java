package br.biblioteca.senac.biblioteca.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza a conexão JDBC. Credenciais devem ser configuradas por variáveis
 * de ambiente.
 */
public final class Conexao {
    private Conexao() {
    }

    public static Connection conectar() throws SQLException {
        String url = System.getenv().getOrDefault("BIBLIOTECA_DB_URL",
                "jdbc:mysql://localhost:3306/biblioteca_trabalho?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true");
        String usuario = System.getenv().getOrDefault("BIBLIOTECA_DB_USER", "root");
        String senha = System.getenv().getOrDefault("BIBLIOTECA_DB_PASSWORD", "");
        return DriverManager.getConnection(url, usuario, senha);
    }
}
