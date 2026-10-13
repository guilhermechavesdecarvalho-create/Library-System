package br.biblioteca.senac.biblioteca.exception;

public class UsuarioBloqueadoException extends RuntimeException {
    
    public UsuarioBloqueadoException(String mensagem) {
        super(mensagem);
    }
}