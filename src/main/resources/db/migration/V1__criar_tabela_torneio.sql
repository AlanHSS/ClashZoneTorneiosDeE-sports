-- V1_criar_tabela_torneio.sql

CREATE TABLE torneios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    inicio TIMESTAMP NOT NULL,
    jogo VARCHAR(50) NOT NULL,
    quantidade_equipes INTEGER NOT NULL,
    criador VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',
    plataforma VARCHAR(50) NOT NULL
);