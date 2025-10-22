package com.alanhss.ClashZone.infra.dtos;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;

import java.time.LocalDateTime;

public record FiltroTorneioDto(String nomeDoTorneio,
                               LocalDateTime inicioDoTorneio,
                               Games jogoDoTorneio,
                               StatusDoTorneio statusDoTorneio,
                               Plataforma plataforma) {
}
