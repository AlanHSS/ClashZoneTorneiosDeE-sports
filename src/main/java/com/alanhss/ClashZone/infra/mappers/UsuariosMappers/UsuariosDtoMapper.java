package com.alanhss.ClashZone.infra.mappers.UsuariosMappers;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.UsuariosDto;
import org.springframework.stereotype.Component;

@Component
public class UsuariosDtoMapper {

    public UsuariosDto toDto(UsuariosDomain usuariosDomain){
        return new UsuariosDto(
                usuariosDomain.id(),
                usuariosDomain.nomeDoUsuario(),
                usuariosDomain.nickname(),
                usuariosDomain.emailDoUsuario(),
                usuariosDomain.senhaDoUsuario()
        );
    }

    public UsuariosDomain toDomain(UsuariosDto usuariosDto){
        return new UsuariosDomain(
                usuariosDto.id(),
                usuariosDto.nomeDoUsuario(),
                usuariosDto.nickname(),
                usuariosDto.emailDoUsuario(),
                usuariosDto.senhaDoUsuario()
        );
    }

    public UsuariosDto validarEPreparar(UsuariosDto usuariosDto){
        String nomeNormalizado = usuariosDto.nomeDoUsuario() != null
                ? usuariosDto.nomeDoUsuario().trim()
                : null;
        String nicknameNormalizado = usuariosDto.nickname() != null
                ? usuariosDto.nickname().trim()
                : null;
        String emailNormalizado = usuariosDto.emailDoUsuario() != null
                ? usuariosDto.emailDoUsuario().trim().toLowerCase()
                : null;

        return new UsuariosDto(
                usuariosDto.id(),
                nomeNormalizado,
                nicknameNormalizado,
                emailNormalizado,
                usuariosDto.senhaDoUsuario()
        );
    }

}
