package com.alanhss.ClashZone.infra.dtos.EquipesDtos;

import com.alanhss.ClashZone.core.enums.Games;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EquipeDto(
        Long id,

        @NotBlank(message = "Nome da equipe é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nomeDaEquipe,

        Long liderId,

        @NotNull(message = "O jogo da equipe é obrigatório.")
        Games jogo,

        LocalDateTime dataCriacao,

        boolean inscrita) {
}
