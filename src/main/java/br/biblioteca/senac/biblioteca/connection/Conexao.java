package br.biblioteca.senac.biblioteca.connection;

import java.sql.Connection;  //conexao com o banco
import java.sql.DriverManager; // configurações internas
import java.sql.SQLException; // sql

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca_trabalho";
    private static final String USUARIO = "root";
    private static final String SENHA = "xk25YZ9094*";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}




