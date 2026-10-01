create database if not exists biblioteca_trabalho;
use biblioteca_trabalho;

create table if not exists autor(
	id int not null primary key auto_increment,
    nome varchar(150) not null,
    nacionalidade varchar(100) not null
);

create table if not exists usuario(
	id int not null primary key auto_increment,
    nome varchar(150) not null,
    CPF varchar(15) not null unique,
    email varchar(200) not null unique,
    livros_emprestados int not null default 0,
    telefone varchar(20) 
    
);
ALTER TABLE usuario ADD COLUMN telefone VARCHAR(20);
desc usuario;


create table if not exists livro(
	id int not null primary key auto_increment,
    titulo varchar(150) not null,
    quantidade_total int not null,
    quantidade_disponivel int  not null, 
    ISBN varchar(50) unique not null,
    editora varchar(150) not null,
    autor_id int not null,
    constraint fk_livro_autor foreign key (autor_id) references autor(id) 
);

create table if not exists emprestimo(
	id int not null primary key auto_increment,
    livro_id int not null, 
    usuario_id int not null, 
    data_emprestimo date not null, 
    data_evolucao date not null, 
    status boolean not null,
    constraint fk_emprestimo_livro foreign key(livro_id) references livro(id),
    constraint fk_emprestimo_usuario foreign key(usuario_id) references usuario(id)
);




