package com.alanhss.ClashZone.infra.mappers.UsuariosMappers;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
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

}
