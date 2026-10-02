# SistemaDeBiblioteca — Java + Maven + JDBC + MySQL

## Preparação
1. Abra `banco/biblioteca.sql` no MySQL Workbench e execute o script.
2. Abra `src/main/java/br/com/senac/biblioteca/conexao/Conexao.java`.
3. Troque `SUA_SENHA_AQUI` pela senha local do usuário `root`.
4. Confirme que o MySQL está executando na porta 3306.
5. Abra a pasta `SistemaDeBiblioteca` no VS Code.
6. Aguarde o Maven baixar a dependência MySQL Connector/J.
7. Execute `Main.java`.

## Resultado esperado
O programa testa o modelo orientado a objetos e, em seguida, tenta estabelecer a primeira conexão JDBC com o banco `biblioteca`.

Nesta versão, o objetivo é CONECTAR Java e MySQL. A persistência/CRUD/DAO será a evolução seguinte.
