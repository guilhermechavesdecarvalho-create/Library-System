# 📚 Biblioteca Senac — Sistema de Gestão

<p align="center">
  <strong>Uma solução web para modernizar a gestão de bibliotecas acadêmicas.</strong>
  <br>
  Organização, praticidade e eficiência em um único lugar.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-Framework-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL 8">
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
</p>

---

## 🎓 Sobre o projeto

O **Biblioteca Senac** é uma aplicação web desenvolvida para facilitar o gerenciamento de bibliotecas acadêmicas, centralizando as principais operações em uma interface intuitiva, responsiva e organizada.

O sistema utiliza Java, Spring Boot, Thymeleaf, JDBC e MySQL, combinando uma estrutura modular com uma identidade visual institucional baseada em **azul-marinho e branco**.

### ✨ Objetivos

- Simplificar as rotinas de gerenciamento da biblioteca.
- Facilitar o controle do acervo e dos usuários cadastrados.
- Tornar o registro de empréstimos e devoluções mais eficiente.
- Melhorar a organização e a visualização das informações.
- Oferecer uma base preparada para futuras evoluções do sistema.

## 🚀 Funcionalidades

| Módulo | Recursos |
|---|---|
| 🔐 **Autenticação** | Cadastro de contas, login por e-mail e senha e encerramento de sessão. |
| 📊 **Painel administrativo** | Indicadores de livros, usuários e empréstimos ativos. |
| 📚 **Acervo** | Cadastro, pesquisa e exclusão de livros por título, autor ou ISBN. |
| 👥 **Usuários** | Consulta, edição e exclusão de usuários sem histórico vinculado. |
| 🔄 **Empréstimos** | Registro de empréstimos com validação de estoque e limite de três empréstimos ativos por usuário. |
| 📅 **Prazos** | Prazo padrão de 14 dias para devolução. |
| ↩️ **Devoluções** | Registro de devoluções e atualização do estoque. |
| 🛡️ **Integridade dos dados** | Transações SQL para manter os registros e o estoque sincronizados. |
| 📱 **Interface responsiva** | Navegação consistente, mensagens de validação e adaptação a diferentes telas. |

## 🧰 Tecnologias utilizadas

- **Java 21** — linguagem principal.
- **Spring Boot** — estrutura da aplicação web.
- **Thymeleaf** — renderização das páginas HTML no servidor.
- **JDBC** — comunicação com o banco de dados.
- **MySQL 8** — armazenamento das informações.
- **Maven 3.9+** — gerenciamento de dependências e compilação.
- **HTML5, CSS3 e JavaScript** — estrutura, apresentação e interatividade da interface.
- **BCrypt** — hash das senhas dos usuários.

## ⚙️ Requisitos

Antes de executar o projeto, instale:

- [Java 21](https://adoptium.net/)
- [Apache Maven 3.9 ou superior](https://maven.apache.org/download.cgi)
- [MySQL 8](https://dev.mysql.com/downloads/mysql/)
- Um editor de código, como o [Visual Studio Code](https://code.visualstudio.com/)

## 🛠️ Instalação e configuração

### 1. Clone o repositório

```bash
git clone https://github.com/guilhermechavesdecarvalho-create/Library-System.git
cd Library-System
```

### 2. Configure o banco de dados

Abra o MySQL Workbench ou outro cliente MySQL e execute o script:

```text
database/biblioteca_trabalho.sql
```

Esse arquivo contém o esquema utilizado pelo sistema.

> **Atenção:** se você já possui um banco com dados importantes, faça um backup antes de executar alterações no esquema.

### 3. Configure as variáveis de ambiente

A aplicação permite configurar a conexão JDBC por meio das seguintes variáveis:

| Variável | Valor padrão | Finalidade |
|---|---|---|
| `BIBLIOTECA_DB_URL` | `jdbc:mysql://localhost:3306/biblioteca_trabalho?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true` | Endereço JDBC do banco. |
| `BIBLIOTECA_DB_USER` | `root` | Usuário do MySQL. |
| `BIBLIOTECA_DB_PASSWORD` | Vazio | Senha do banco. |

No Windows PowerShell, configure os valores para a sessão atual:

```powershell
$env:BIBLIOTECA_DB_USER="root"
$env:BIBLIOTECA_DB_PASSWORD="sua-senha"
```

Se o banco estiver em outro endereço, configure também `BIBLIOTECA_DB_URL`.

**Segurança:** substitua os exemplos pelos dados do seu ambiente. Nunca publique senhas reais, tokens ou credenciais no repositório.

### 4. Execute a aplicação

Na raiz do projeto, execute:

```bash
mvn spring-boot:run
```

Quando a inicialização terminar, abra o navegador em:

**http://localhost:8080/**

Crie uma conta e entre no sistema para acessar as funcionalidades disponíveis.

### 5. Gere o pacote da aplicação

Para executar os testes e gerar o pacote:

```bash
mvn clean package
```

O Maven disponibilizará o artefato gerado na pasta `target/`, conforme a configuração do projeto.

## 🗂️ Estrutura do projeto

```text
Library-System/
├── database/
│   └── biblioteca_trabalho.sql
├── src/
│   ├── main/
│   │   ├── java/br/biblioteca/senac/biblioteca/
│   │   │   ├── connection/    # Conexão JDBC
│   │   │   ├── controller/    # Rotas e autenticação
│   │   │   ├── dao/           # Acesso aos dados
│   │   │   ├── exception/     # Exceções de negócio
│   │   │   ├── model/         # Modelos da aplicação
│   │   │   ├── service/       # Regras de negócio
│   │   │   └── LibrarySystemApplication.java
│   │   └── resources/
│   │       ├── static/        # CSS, imagens e recursos
│   │       ├── templates/     # Páginas Thymeleaf
│   │       └── application.properties
│   └── test/                  # Testes automatizados
├── pom.xml
├── README.md
└── .gitignore
```

## 🔒 Segurança e limitações conhecidas

- As senhas de novas contas são armazenadas usando hash BCrypt.
- Contas antigas sem `senha_hash` precisam criar uma nova conta. Senhas antigas em texto puro não são aceitas.
- A recuperação automática de senha depende da configuração de um serviço de e-mail e de um fluxo seguro de tokens. A tela, por si só, não realiza o envio.
- A coluna `data_evolucao` foi mantida por compatibilidade com o banco original e representa a data prevista de devolução.
- O sistema utiliza JDBC e DAOs existentes para acesso aos dados.

### 📌 Melhorias recomendadas

Para preparar o projeto para um ambiente de produção, recomenda-se:

- Implementar migrações versionadas com Flyway.
- Adicionar testes de integração com MySQL.
- Implementar logs estruturados e monitoramento.
- Revisar as permissões e a autorização por perfil.
- Configurar HTTPS e variáveis de ambiente no servidor.
- Implementar recuperação de senha com tokens seguros e expiração.
- Configurar backups e procedimentos de recuperação do banco.

## 🧪 Testes

Execute os testes automatizados com:

```bash
mvn test
```

Para verificar também a compilação e a geração do pacote:

```bash
mvn clean package
```

O sucesso dos comandos confirma que as etapas executadas terminaram sem erros, mas não substitui os testes funcionais completos nem a validação em um ambiente de produção.

## 🤝 Contribuição

Sugestões e melhorias são bem-vindas!

1. Crie uma branch para sua alteração.
2. Faça as modificações e execute os testes.
3. Registre suas alterações em um commit.
4. Envie a branch ao GitHub e abra um Pull Request.

## 👨‍💻 Desenvolvimento

Projeto desenvolvido no contexto acadêmico do curso Técnico em Desenvolvimento de Sistemas do **SENAC Santa Maria**, com foco na aplicação prática de desenvolvimento web, persistência de dados e organização de software.

## 📄 Licença

Nenhuma licença específica foi informada neste README. Antes de permitir reutilização ou distribuição por terceiros, defina e adicione a licença apropriada ao repositório.

---

<p align="center">
  <strong>Biblioteca Senac</strong>
  <br>
  <em>Tecnologia e organização a serviço do conhecimento.</em>
</p>
