CREATE TABLE inscricoes_torneio (
    id BIGSERIAL PRIMARY KEY,
    torneio_id BIGINT NOT NULL,
    equipe_id BIGINT NOT NULL,
    status_inscricao VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    motivo_recusa TEXT,
    data_inscricao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_inscricao_torneio
        FOREIGN KEY (torneio_id)
        REFERENCES torneios(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_inscricao_equipe
        FOREIGN KEY (equipe_id)
        REFERENCES equipes(id)
        ON DELETE CASCADE,

    CONSTRAINT check_status_inscricao
        CHECK (status_inscricao IN ('PENDENTE', 'APROVADA', 'RECUSADA', 'CANCELADA')),

    CONSTRAINT uk_torneio_equipe UNIQUE (torneio_id, equipe_id)
);