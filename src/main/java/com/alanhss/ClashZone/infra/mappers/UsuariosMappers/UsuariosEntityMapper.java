package com.alanhss.ClashZone.infra.mappers.UsuariosMappers;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.infra.persistence.UsuariosEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuariosEntityMapper {

    public UsuariosEntity toEntity(UsuariosDomain usuariosDomain){
        return new UsuariosEntity(
                usuariosDomain.id(),
                usuariosDomain.nomeDoUsuario(),
                usuariosDomain.nickname(),
                usuariosDomain.emailDoUsuario(),
                usuariosDomain.senhaDoUsuario()
        );
    }

    public UsuariosDomain toDomain(UsuariosEntity usuariosEntity){
        return new UsuariosDomain(
                usuariosEntity.getId(),
                usuariosEntity.getNomeDoUsuario(),
                usuariosEntity.getNickname(),
                usuariosEntity.getEmailDoUsuario(),
                usuariosEntity.getSenhaDoUsuario()
        );
    }

}
