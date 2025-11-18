package com.alanhss.ClashZone.core.domain;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;

import java.time.LocalDateTime;


public record TorneioDomain(Long id,
                            String nomeDoTorneio,
                            String descricaoDoTorneio,
                            LocalDateTime inicioDoTorneio,
                            Games jogoDoTorneio,
                            Integer quantidadeDeEquipes,
                            Long criadorId,
                            StatusDoTorneio statusDoTorneio,
                            Plataforma plataforma,
                            LocalDateTime dataCriacao
) {}
