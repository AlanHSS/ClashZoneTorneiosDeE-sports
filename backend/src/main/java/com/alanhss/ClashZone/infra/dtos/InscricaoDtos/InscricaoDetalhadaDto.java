package com.alanhss.ClashZone.infra.dtos.InscricaoDtos;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.StatusInscricao;

import java.time.LocalDateTime;

public record InscricaoDetalhadaDto(

        Long id,

        Long torneioId,
        String nomeTorneio,
        LocalDateTime inicioTorneio,
        Games jogoTorneio,

        Long equipeId,
        String nomeEquipe,
        Long liderId,

        StatusInscricao statusInscricao,
        String motivoRecusa,
        LocalDateTime dataInscricao
) {}
