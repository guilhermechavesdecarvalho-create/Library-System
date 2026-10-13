package br.biblioteca.senac.biblioteca.exception;

public class LimiteEmprestimosExcedidoException extends RuntimeException {
    
    public LimiteEmprestimosExcedidoException(String mensagem) {
        super(mensagem);
    }
}