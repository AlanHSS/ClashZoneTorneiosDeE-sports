package com.alanhss.ClashZone.core.domain;

import com.alanhss.ClashZone.core.enums.StatusInscricao;

import java.time.LocalDateTime;

public record InscricaoTorneioDomain(
        Long id,
        Long torneioId,
        Long equipeId,
        StatusInscricao statusInscricao,
        String motivoRecusa,
        LocalDateTime dataInscricao
) {}
