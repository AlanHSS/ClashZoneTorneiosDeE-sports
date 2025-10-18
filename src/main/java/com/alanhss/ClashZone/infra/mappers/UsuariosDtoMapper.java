package com.alanhss.ClashZone.infra.mappers;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.infra.dtos.UsuariosDto;

public class UsuariosDtoMapper {

    public UsuariosDto toDto(UsuariosDomain usuariosDomain){
        return new UsuariosDto(
                usuariosDomain.id(),
                usuariosDomain.nomeDoUsuario(),
                usuariosDomain.emailDoUsuario(),
                usuariosDomain.senhaDoUsuario()
        );
    }

    public UsuariosDomain toDomain(UsuariosDto usuariosDto){
        return new UsuariosDomain(
                usuariosDto.id(),
                usuariosDto.nomeDoUsuario(),
                usuariosDto.emailDoUsuario(),
                usuariosDto.senhaDoUsuario()
        );
    }

}
