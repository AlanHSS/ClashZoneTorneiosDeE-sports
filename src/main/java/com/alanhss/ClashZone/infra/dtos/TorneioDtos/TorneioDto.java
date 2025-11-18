package com.alanhss.ClashZone.infra.dtos;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record TorneioDto(

        Long id,

        @NotBlank(message = "Nome do torneio é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nomeDoTorneio,

        String descricaoDoTorneio,

        @NotNull(message = "A data de início do torneio é obrigatória.")
        @Future(message = "A data de início deve estar no futuro.")
        LocalDateTime inicioDoTorneio,

        @NotNull(message = "O jogo do torneio é obrigatório.")
        Games jogoDoTorneio,

        @NotNull(message = "A quantidade de equipes é obrigatória.")
        @Min(4)
        @Max(128)
        Integer quantidadeDeEquipes,

        Long criadorId,

        StatusDoTorneio statusDoTorneio,

        @NotNull(message = "A plataforma é obrigatória.")
        Plataforma plataforma,

        LocalDateTime dataCriacao) {
}
