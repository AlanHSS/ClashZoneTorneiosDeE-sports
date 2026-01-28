package com.alanhss.ClashZone.core.domain;

import com.alanhss.ClashZone.core.enums.Games;

import java.time.LocalDateTime;

public record EquipeDomain(Long id,
                           String nomeDaEquipe,
                           Long liderId,
                           Games jogo,
                           LocalDateTime dataCriacao,
                           boolean inscrita
) {}
