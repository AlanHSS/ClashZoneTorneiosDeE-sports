package com.alanhss.ClashZone.infra.mappers.UsuariosMappers;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.AtualizarUsuariosDto;
import org.springframework.stereotype.Component;

@Component
public class UsuariosAtualizarMapper {

    public UsuariosDomain toDomain(Long id, AtualizarUsuariosDto atualizarUsuariosDto){
        return new UsuariosDomain(
                id,
                atualizarUsuariosDto.nomeDoUsuario(),
                null,
                atualizarUsuariosDto.emailDoUsuario(),
                atualizarUsuariosDto.senhaDoUsuario()
        );
    }

    public AtualizarUsuariosDto validarEPrepararAtualizacao(AtualizarUsuariosDto atualizarUsuariosDto){
        String nomeNormalizado = atualizarUsuariosDto.nomeDoUsuario() != null
                ? atualizarUsuariosDto.nomeDoUsuario().trim()
                : null;

        return new AtualizarUsuariosDto(
                nomeNormalizado,
                atualizarUsuariosDto.emailDoUsuario(),
                atualizarUsuariosDto.senhaDoUsuario()
        );
    }
}
