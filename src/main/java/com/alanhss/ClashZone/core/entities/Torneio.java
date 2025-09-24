package com.alanhss.ClashZone.core.entities;

import com.alanhss.ClashZone.core.enums.Games;

import java.time.LocalDateTime;


public record Torneio(Integer id,
                      String nomeDoTorneio,
                      String descricaoDoTorneio,
                      LocalDateTime inicioDoTorneio,
                      Games jogoDoTorneio,
                      Integer quantidadeDeEquipes,
                      Integer quantidadeDePessoasPorEquipe,
                      String criadorDoTorneio
) {}
