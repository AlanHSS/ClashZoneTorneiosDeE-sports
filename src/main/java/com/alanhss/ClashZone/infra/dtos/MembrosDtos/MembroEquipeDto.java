package com.alanhss.ClashZone.infra.dtos.MembrosDtos;

import com.alanhss.ClashZone.core.enums.TipoMembro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MembroEquipeDto(
        Long id,

        Long equipeId,

        @NotBlank(message = "Nickname é obrigatório")
        @Size(min = 3, max = 50, message = "Nickname deve ter entre 3 e 50 caracteres")
        String nickname,

        @NotNull(message = "Tipo do membro é obrigatório")
        TipoMembro tipo,

        @Size(max = 50, message = "Rank deve ter no máximo 50 caracteres")
        String rank,

        LocalDateTime dataAdicao
) {
}
