[readme_md.md](https://github.com/user-attachments/files/33081311/readme_md.md)
# 📚 Sistema de Gerenciamento de Biblioteca (Library System)

<div align="center">

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=for-the-badge)]()

Sistema backend desenvolvido em **Java** com arquitetura baseada em **DAO (Data Access Object)** e banco de dados **MySQL** para automatizar o controle de acervos, usuários e empréstimos de uma biblioteca.

</div>

---

## 🚀 Funcionalidades Principais

* **Gerenciamento de Livros:** Cadastro, consulta, atualização e exclusão de obras literárias (título, autor, ISBN, quantidade de exemplares disponíveis).
* **Gestão de Usuários:** Cadastro de leitores/membros da biblioteca com validações básicas.
* **Controle de Empréstimos:** Registro de retirada e devolução de livros, controlando prazos e a disponibilidade do estoque.
* **Persistência de Dados Robusta:** Comunicação direta com banco de dados relacional via JDBC utilizando o padrão DAO.

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java (JDK 11 ou superior)
* **Banco de Dados:** MySQL
* **Gerenciador de Dependências / Build:** Apache Maven
* **Arquitetura:** Padrão DAO (Separação de responsabilidades entre regras de negócio, interface e persistência)

---

## 📋 Pré-requisitos

Antes de clonar e executar o projeto, certifique-se de ter instalado em sua máquina:
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (versão 11 ou superior)
* [Apache Maven](https://maven.apache.org/)
* [MySQL Server](https://dev.mysql.com/downloads/) (ou um ambiente containerizado via Docker)
* Uma IDE de sua preferência (IntelliJ IDEA, Eclipse ou VS Code)

---

## ⚙️ Configuração e Instalação

### 1. Clonar o Repositório
Abra o seu terminal e execute o comando:
```bash
git clone https://github.com/guilhermechavesdecarvalho-create/Library-System.git
```

### 2. Configurar o Banco de Dados
Acesse o seu SGBD MySQL e crie o banco de dados da aplicação:
```sql
CREATE DATABASE biblioteca_trabalho;
USE biblioteca_trabalho;
```
*(Nota: Certifique-se de executar os scripts de criação das tabelas `livro`, `usuario` e `emprestimo` conforme o modelo relacional do projeto).*

### 3. Configurar Conexão
Atualize as credenciais de acesso ao banco de dados (URL, usuário e senha) no arquivo de configuração ou classe de conexão JDBC do projeto (`ConexaoMySql.java` ou similar).

### 4. Executar o Projeto
Abra o projeto na sua IDE favorita, deixe o Maven carregar as dependências e execute a classe principal contendo o método `main`.

---

## 🗄️ Modelo Entidade-Relacionamento (DER)

*(Sugestão: Adicione aqui uma imagem do modelo de banco de dados para ilustrar o projeto visualmente)*
```text
[ LIVRO ] 1 -------- N [ EMPRISTIMO ] N -------- 1 [ USUARIO ]
```

---

## 🗺️ Próximos Passos e Melhorias Planejadas

- [ ] Implementação de **Transações (Commit/Rollback)** para operações críticas de empréstimo.
- [ ] Tratamento de exceções personalizadas (ex: `LivroIndisponivelException`).
- [ ] Adição de testes unitários com **JUnit**.
- [ ] Migração para pool de conexões (HikariCP).

---

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! Se você tiver sugestões para melhorar o sistema:
1. Faça um Fork do projeto (`https://github.com/guilhermechavesdecarvalho-create/Library-System/fork`)
2. Crie uma Branch para sua Feature (`git checkout -b feature/MinhaFeature`)
3. Faça o Commit das suas alterações (`git commit -m 'Add: Minha nova feature'`)
4. Faça o Push para a Branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 👤 Autor

Desenvolvido por **Guilherme Chaves de Carvalho**.
* [GitHub](https://github.com/guilhermechavesdecarvalho-create)
