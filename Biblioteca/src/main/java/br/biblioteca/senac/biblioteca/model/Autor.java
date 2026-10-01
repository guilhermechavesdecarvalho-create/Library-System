package br.biblioteca.senac.biblioteca.model;

public class Autor {
    
    private long id;
    private String nome;
    private String nacionalidade;
    
    public Autor(long id, String nome, String nacionalidade){
        if(id <= 0){
          throw new IllegalArgumentException ("O id DEVE ser maior que 0!");
        }
        
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException ("O nome nao pode ser vazio!");
        }
        
        if(nacionalidade == null || nacionalidade.trim().isEmpty()){
            throw new IllegalArgumentException ("A nacionalidade nao pode ser vazia!");
        }
        
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }
    
    
}