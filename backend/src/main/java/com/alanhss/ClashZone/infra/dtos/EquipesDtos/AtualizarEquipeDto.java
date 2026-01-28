package com.alanhss.ClashZone.infra.dtos.EquipesDtos;

import com.alanhss.ClashZone.core.enums.Games;

public record AtualizarEquipeDto(String nomeDaEquipe,
                                 Games jogo,
                                 boolean inscrita) {
}
