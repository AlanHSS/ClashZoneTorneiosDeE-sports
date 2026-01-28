package com.alanhss.ClashZone.infra.mappers.UsuariosMappers;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuariosEntityMapper {

    public UsuariosEntity toEntity(UsuariosDomain usuariosDomain){
        return new UsuariosEntity(
                usuariosDomain.id(),
                usuariosDomain.nomeDoUsuario(),
                usuariosDomain.nickname(),
                usuariosDomain.emailDoUsuario(),
                usuariosDomain.senhaDoUsuario(),
                usuariosDomain.dataCriacao(),
                usuariosDomain.role() != null ? usuariosDomain.role() : Role.USER
        );
    }

    public UsuariosDomain toDomain(UsuariosEntity usuariosEntity){
        return new UsuariosDomain(
                usuariosEntity.getId(),
                usuariosEntity.getNomeDoUsuario(),
                usuariosEntity.getNickname(),
                usuariosEntity.getEmailDoUsuario(),
                usuariosEntity.getSenhaDoUsuario(),
                usuariosEntity.getDataCriacao(),
                usuariosEntity.getRole()
        );
    }

}
