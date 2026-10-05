package br.biblioteca.senac.biblioteca.model;

import java.time.LocalDate;

public class Emprestimo {
    
    private long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean status;

    public Emprestimo(){
        this.dataEmprestimo = LocalDate.now(); // pega a data atual do sistema
        this.dataDevolucao = this.dataEmprestimo.plusDays(14); // devolver em 14 dias
        this.status = true; // empréstimo ativo
    }
    
    public Emprestimo(long id, Livro livro, Usuario usuario, boolean status){
        if(id <= 0 ){
            throw new IllegalArgumentException ("O id deve ser maior que zero!");
        }
        
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now(); // pega a data atual do sistema
        this.dataDevolucao = this.dataEmprestimo.plusDays(14); // devolver em 14 dias
        this.status = status;
    }

    public long getId() {
        return id;
    }

    
    public void setId(long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

