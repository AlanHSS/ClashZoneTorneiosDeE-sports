CREATE TABLE equipes (
    id BIGSERIAL PRIMARY KEY,
    nome_da_equipe VARCHAR(100) NOT NULL,
    lider_id BIGINT NOT NULL,
    jogo VARCHAR(50) NOT NULL,
    inscrita BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_equipes_lider FOREIGN KEY (lider_id) REFERENCES usuarios(id) ON DELETE CASCADE
);