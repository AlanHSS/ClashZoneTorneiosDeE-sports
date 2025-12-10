package com.alanhss.ClashZone.infra.dtos.InscricaoDtos;

import com.alanhss.ClashZone.core.enums.StatusInscricao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizarInscricaoDto(

        @NotNull(message = "Status da inscrição é obrigatório")
        StatusInscricao statusInscricao,

        @Size(max = 500, message = "Motivo da recusa deve ter no máximo 500 caracteres")
        String motivoRecusa
) {
}
