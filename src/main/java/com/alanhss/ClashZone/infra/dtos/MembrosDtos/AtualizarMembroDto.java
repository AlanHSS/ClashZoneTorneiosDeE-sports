package com.alanhss.ClashZone.infra.dtos.MembrosDtos;

import com.alanhss.ClashZone.core.enums.TipoMembro;
import jakarta.validation.constraints.Size;

public record AtualizarMembroDto(
        @Size(min = 3, max = 50, message = "Nickname deve ter entre 3 e 50 caracteres")
        String nickname,

        TipoMembro tipo,

        @Size(max = 50, message = "Rank deve ter no máximo 50 caracteres")
        String rank
) {
}
