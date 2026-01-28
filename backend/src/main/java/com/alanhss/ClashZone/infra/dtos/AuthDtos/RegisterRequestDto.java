package com.alanhss.ClashZone.infra.dtos.AuthDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nomeDoUsuário,

        @NotBlank(message = "Nickname é obrigatório")
        @Size(min = 3, max = 50, message = "Nickname deve ter entre 3 e 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$",
                message = "Nickname deve conter apenas letras, números e underscore")
        String nickname,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String emailDoUsuario,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$",
                message = "Senha deve conter pelo menos uma letra e um número")
        String senhaDoUsuario
) {}
