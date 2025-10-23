package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosEntityMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<UsuariosDomain> listarUsuarios() {
        List<UsuariosEntity> entityList = usuariosRepository.findAll();

        return entityList.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public UsuariosDomain atualizarUsuario(Long id, UsuariosDomain usuariosDomain) {
        UsuariosEntity usurioExistente = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        if (usuariosDomain.nomeDoUsuario() != null){
            usurioExistente.setNomeDoUsuario(usuariosDomain.nomeDoUsuario());
        }
        if (usuariosDomain.emailDoUsuario() != null){
            usurioExistente.setEmailDoUsuario(usuariosDomain.emailDoUsuario());
        }
        if (usuariosDomain.senhaDoUsuario() != null){
            usurioExistente.setSenhaDoUsuario(usuariosDomain.senhaDoUsuario());
        }

        UsuariosEntity usuarioAtulizado = usuariosRepository.save(usurioExistente);
        return mapper.toDomain(usuarioAtulizado);
    }
}
