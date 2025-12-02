CREATE TABLE membros_equipe (
    id BIGSERIAL PRIMARY KEY,
    equipe_id BIGINT NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    rank VARCHAR(50),
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_membros_equipe
        FOREIGN KEY (equipe_id)
        REFERENCES equipes(id)
        ON DELETE CASCADE,

    CONSTRAINT check_tipo
        CHECK (tipo IN ('TITULAR', 'RESERVA'))
);

CREATE UNIQUE INDEX idx_equipe_membro_nickname
    ON membros_equipe(equipe_id, nickname);