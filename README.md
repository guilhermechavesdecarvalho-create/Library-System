# Biblioteca Senac — Sistema de Gestão

Aplicação web para gestão de acervo, usuários, empréstimos e devoluções. O projeto preserva a base Java/Spring Boot, Thymeleaf, JDBC e MySQL e organiza as telas com identidade visual institucional azul-marinho e branco.

## Requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

## 1. Configurar o banco de dados

1. Abra o MySQL Workbench ou um cliente MySQL.
2. Execute o script `database/biblioteca_trabalho.sql`.
3. Configure as variáveis de ambiente antes de iniciar a aplicação:

| Variável | Padrão | Descrição |
|---|---|---|
| `BIBLIOTECA_DB_URL` | `jdbc:mysql://localhost:3306/biblioteca_trabalho?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true` | URL JDBC do banco |
| `BIBLIOTECA_DB_USER` | `root` | Usuário do banco |
| `BIBLIOTECA_DB_PASSWORD` | vazio | Senha do banco |

No Windows PowerShell, por exemplo:

```powershell
$env:BIBLIOTECA_DB_USER="root"
$env:BIBLIOTECA_DB_PASSWORD="sua-senha"
```

Não grave credenciais reais no repositório. Em bancos criados por versões anteriores, execute novamente o script para aplicar as colunas compatíveis. Faça backup antes de alterar bancos com dados importantes.

## 2. Executar

Na raiz do projeto:

```bash
mvn spring-boot:run
```

Acesse `http://localhost:8080/`, crie uma conta e entre no sistema. Para gerar o pacote:

```bash
mvn clean package
```

## Funcionalidades incluídas

- Cadastro de contas com senha armazenada em hash BCrypt.
- Login por e-mail e senha e encerramento de sessão.
- Painel com contagem de livros, usuários e empréstimos ativos.
- Cadastro, pesquisa e exclusão de livros por título, autor ou ISBN.
- Consulta, edição de dados cadastrais e exclusão de usuários quando não houver histórico vinculado.
- Registro de empréstimos com validação de estoque e limite de três empréstimos ativos por usuário.
- Prazo padrão de 14 dias.
- Registro de devolução e reposição de estoque.
- Transação SQL para manter o registro de empréstimo/devolução e o estoque sincronizados.
- Interface responsiva com navegação consistente e mensagens de validação.

## Observações importantes

- A recuperação automática de senha ainda depende de configurar um serviço de e-mail e um fluxo seguro de token. A tela informa essa limitação sem afirmar que enviou mensagens.
- Contas antigas sem `senha_hash` precisam criar uma conta nova; senhas antigas não são aceitas nem armazenadas em texto puro.
- A coluna `data_evolucao` foi mantida por compatibilidade com o banco original e representa a data prevista de devolução.
- A aplicação utiliza JDBC e DAOs existentes. Para produção, recomenda-se adicionar migrações versionadas (Flyway), logs estruturados, testes de integração com MySQL e controles adicionais de autorização por perfil.

## Estrutura principal

- `src/main/java/.../controller`: rotas web e autenticação.
- `src/main/java/.../service`: regras de negócio.
- `src/main/java/.../dao`: acesso a dados.
- `src/main/java/.../connection/Conexao.java`: conexão JDBC por variáveis de ambiente.
- `src/main/resources/templates`: páginas Thymeleaf.
- `src/main/resources/static/app.css`: estilos compartilhados.
- `database/biblioteca_trabalho.sql`: criação e compatibilização do esquema MySQL.
