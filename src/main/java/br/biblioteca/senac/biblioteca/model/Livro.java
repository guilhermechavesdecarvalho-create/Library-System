package br.biblioteca.senac.biblioteca.model;

public class Livro {
    
    private long id; // Adicionado o ID
    private String titulo;
    private Autor autor;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private String isbn;
    private String editora;
    
    // Construtor vazio útil para DAOs
    public Livro() {
    }

    public Livro(String titulo, Autor autor, int quantidade_total, String isbn, String editora){
        if(titulo == null || titulo.trim().isEmpty()){
            throw new IllegalArgumentException ("O titulo NAO pode ser vazio!");
        }
        
         if(autor == null){
            throw new IllegalArgumentException ("O autor NAO pode ser vazio!");
        }
         
         if(quantidade_total <= 0){
            throw new IllegalArgumentException("A quantidade total  DEVE ser maior que zero!");
        }
         
         if(isbn == null || isbn.trim().isEmpty()){
            throw new IllegalArgumentException ("O ISBN do livro  NAO pode ser vazio! Ele deve ser informado");
        }
         
         if(editora == null || editora.trim().isEmpty()){
            throw new IllegalArgumentException ("A editora NAO pode ser vazia!");
        }
        
         this.titulo = titulo;
         this.autor = autor;
         this.quantidadeTotal = quantidade_total;
         this.quantidadeDisponivel = quantidade_total;
         this.isbn = isbn;
         this.editora = editora;
    }
    
    public boolean emprestar(int quantidade){
        if(this.quantidadeDisponivel > 0  && this.quantidadeDisponivel >= quantidade){
            this.quantidadeDisponivel -= quantidade;
            return true;
        }
        return false;
    }
    
    public boolean devolver(int quantidade){
        if(quantidade > 0 && (this.quantidadeDisponivel + quantidade) <= this.quantidadeTotal){
            this.quantidadeDisponivel += quantidade;
            return true;
        }
        return false;
    }
    
    public void exibirStatus(){
        System.out.println("=====DADOS DOS LIVROS=====");
        System.out.println("|ID: " + id + "|");
        System.out.println("|Titulo: " + titulo + "|");
        System.out.println("|Autor: " + autor.getNome() + "|");
        System.out.println("|Quantidade Total: " + quantidadeTotal + "|");
        System.out.println("|Quantidade Disponivel: " + quantidadeDisponivel + "|");
        System.out.println("|ISBN: " + isbn + "|");
        System.out.println("|Editora: " + editora + "|");
        System.out.println("==========================\n");
    }

    // --- GETTERS E SETTERS ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }
}