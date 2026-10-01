package br.biblioteca.senac.biblioteca.model;

public class Usuario {
    
    private long id; 
    private String nome;
    private String CPF;
    private String email;
    private String telefone;
    private int livrosEmprestados;
    

    public Usuario() {
    }

    public Usuario(String nome, String CPF, String email, String telefone) {
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException ("O nome NAO pode ser vazio!");
        }
        
        if(CPF == null || CPF.trim().isEmpty()){
            throw new IllegalArgumentException ("O CPF NAO pode ser vazio!");
        }
        
        if(!CPF.matches("\\d{11}")){
            throw new IllegalArgumentException("O CPF deve conter APENAS 11 digitos numericos");
        }
        
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("O email NAO pode ser vazio!");
        }
        
        this.nome = nome;
        this.CPF = CPF;
        this.email = email;
        this.telefone = telefone;
        this.livrosEmprestados = 0;
    }
    
    // --- GETTER E SETTER DO ID (O que estava faltando) ---
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCPF() { return CPF; }
    public void setCPF(String CPF) { this.CPF = CPF; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public int getLivrosEmprestados() { return livrosEmprestados; }
    public void setLivrosEmprestados(int livrosEmprestados) { this.livrosEmprestados = livrosEmprestados; }

    public boolean adicionarLivroEmprestado(){
        if(this.livrosEmprestados < 3){
            this.livrosEmprestados ++;
            System.out.println("O emprestimo do livro foi realizado com sucesso!");
            return true;
        }
        System.out.println("O usuario atingiu o limite maximo de emprestimo! Emprestimo negado!");
        return false;
    }
    
    public boolean removerLivroEmprestado(){
        if(this.livrosEmprestados > 0){
            this.livrosEmprestados --;
            System.out.println("O livro foi devolvido com sucesso!");
            return true;
        }
        System.out.println("O usuario nao possui nenhum livro a ser devolvido!");
        return false;
    }
    
    public void exibirStatus(){
        System.out.println("=== DADOS DO USUARIO ===");
        System.out.println("|ID: " + id + "|");
        System.out.println("|Nome: " + nome + "|");
        System.out.println("|CPF: "  + CPF + "|");
        System.out.println("|E-mail: " + email + "|");
        System.out.println("|Telefone: " + telefone + "|");
        System.out.println("|Livros Emprestados: " + livrosEmprestados + "|");
        System.out.println("=====================================\n");
    }
}
