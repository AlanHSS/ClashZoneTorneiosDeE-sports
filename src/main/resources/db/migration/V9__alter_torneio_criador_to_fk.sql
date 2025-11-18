ALTER TABLE torneios DROP COLUMN criador_do_torneio;

ALTER TABLE torneios
ADD COLUMN criador_id BIGINT NOT NULL;

ALTER TABLE torneios
ADD CONSTRAINT fk_torneio_criador
FOREIGN KEY (criador_id)
REFERENCES usuarios(id)
ON DELETE CASCADE;