-- Biblioteca Senac | Script idempotente para MySQL 8+
CREATE DATABASE IF NOT EXISTS biblioteca_trabalho CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_trabalho;

CREATE TABLE IF NOT EXISTS autor (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  nacionalidade VARCHAR(100) NOT NULL,
  UNIQUE KEY uq_autor_nome_nacionalidade (nome, nacionalidade)
);

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  cpf VARCHAR(15) NOT NULL UNIQUE,
  email VARCHAR(200) NOT NULL UNIQUE,
  livros_emprestados INT NOT NULL DEFAULT 0,
  telefone VARCHAR(20),
  senha_hash VARCHAR(100) NULL,
  criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS livro (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  quantidade_total INT NOT NULL,
  quantidade_disponivel INT NOT NULL,
  isbn VARCHAR(50) NOT NULL UNIQUE,
  editora VARCHAR(150) NOT NULL,
  autor_id BIGINT NOT NULL,
  CONSTRAINT fk_livro_autor FOREIGN KEY (autor_id) REFERENCES autor(id),
  CONSTRAINT chk_livro_quantidades CHECK (quantidade_total >= 0 AND quantidade_disponivel >= 0 AND quantidade_disponivel <= quantidade_total)
);

CREATE TABLE IF NOT EXISTS emprestimo (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  livro_id BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL,
  data_emprestimo DATE NOT NULL,
  data_evolucao DATE NOT NULL COMMENT 'Data prevista para devolução; mantido para compatibilidade com a versão inicial',
  status BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'TRUE = empréstimo ativo; FALSE = devolvido',
  CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id) REFERENCES livro(id),
  CONSTRAINT fk_emprestimo_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
  INDEX idx_emprestimo_status (status),
  INDEX idx_emprestimo_usuario_status (usuario_id, status)
);

-- Migração compatível com bancos criados por versões anteriores (MySQL 8.0).
SET @tem_senha_hash = (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'usuario' AND COLUMN_NAME = 'senha_hash'
);
SET @sql_migracao = IF(@tem_senha_hash = 0,
  'ALTER TABLE usuario ADD COLUMN senha_hash VARCHAR(100) NULL',
  'SELECT 1');
PREPARE stmt_migracao FROM @sql_migracao;
EXECUTE stmt_migracao;
DEALLOCATE PREPARE stmt_migracao;
