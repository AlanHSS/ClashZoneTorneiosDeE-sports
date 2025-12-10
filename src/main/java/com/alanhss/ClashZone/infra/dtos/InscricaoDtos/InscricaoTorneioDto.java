package com.alanhss.ClashZone.infra.dtos.InscricaoDtos;

import com.alanhss.ClashZone.core.enums.StatusInscricao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InscricaoTorneioDto(
        Long id,

        @NotNull(message = "ID do torneio é obrigatório")
        Long torneioId,

        @NotNull(message = "ID da equipe é obrigatório")
        Long equipeId,

        StatusInscricao statusInscricao,

        String motivoRecusa,

        LocalDateTime dataInscricao
) {}
