CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome_do_usuario VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    email_do_usuario VARCHAR(150) NOT NULL UNIQUE,
    senha_do_usuario VARCHAR(255) NOT NULL
);
