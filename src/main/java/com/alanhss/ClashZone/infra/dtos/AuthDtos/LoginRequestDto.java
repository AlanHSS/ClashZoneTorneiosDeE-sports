package com.alanhss.ClashZone.infra.dtos.AuthDtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String emailDoUsuario,

        @NotBlank(message = "Senha é obrigatória")
        String senhaDoUsuario) {
}
