package br.biblioteca.senac.biblioteca.exception;

public class LivroIndisponivelException extends RuntimeException {
    
    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}