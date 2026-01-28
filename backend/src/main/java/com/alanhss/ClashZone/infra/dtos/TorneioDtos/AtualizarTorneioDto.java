package com.alanhss.ClashZone.infra.dtos;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;

import java.time.LocalDateTime;

public record AtualizarTorneioDto(String nomeDoTorneio,
                                  String descricaoDoTorneio,
                                  LocalDateTime inicioDoTorneio,
                                  Games jogoDoTorneio,
                                  Integer quantidadeDeEquipes,
                                  StatusDoTorneio statusDoTorneio,
                                  Plataforma plataforma) {
}
