package com.alanhss.ClashZone.infra.mappers;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.infra.persistence.UsuariosEntity;

public class UsuariosEntityMapper {

    public UsuariosEntity toEntity(UsuariosDomain usuariosDomain){
        return new UsuariosEntity(
                usuariosDomain.id(),
                usuariosDomain.nomeDoUsuario(),
                usuariosDomain.emailDoUsuario(),
                usuariosDomain.senhaDoUsuario()
        );
    }

    public UsuariosDomain toDomain(UsuariosEntity usuariosEntity){
        return new UsuariosDomain(
                usuariosEntity.getId(),
                usuariosEntity.getNomeDoUsuario(),
                usuariosEntity.getEmailDoUsuario(),
                usuariosEntity.getSenhaDoUsuario()
        );
    }

}
