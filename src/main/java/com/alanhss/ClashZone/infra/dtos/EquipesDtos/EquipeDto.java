package com.alanhss.ClashZone.infra.dtos.EquipesDtos;

import com.alanhss.ClashZone.core.enums.Games;

import java.time.LocalDateTime;

public record EquipeDto(Long id,
                        String nomeDaEquipe,
                        Long liderId,
                        Games jogo,
                        LocalDateTime dataCriacao,
                        boolean inscrita) {
}
