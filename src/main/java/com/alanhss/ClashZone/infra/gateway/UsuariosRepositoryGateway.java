package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.infra.mappers.UsuariosEntityMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.UsuariosEntity;
import com.alanhss.ClashZone.infra.persistence.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuariosRepositoryGateway implements UsuariosGateway {

    private final UsuariosRepository usuariosRepository;
    private final UsuariosEntityMapper mapper;

    @Override
    public UsuariosDomain criarUsuario(UsuariosDomain usuariosDomain) {
        UsuariosEntity usuariosEntity = mapper.toEntity(usuariosDomain);
        UsuariosEntity novoUsuario = usuariosRepository.save(usuariosEntity);
        return mapper.toDomain(novoUsuario);
    }
}
