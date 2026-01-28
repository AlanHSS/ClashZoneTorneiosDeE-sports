package com.alanhss.ClashZone.infra.mappers.TorneiosMappers;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TorneioEntityMapper {

    private final UsuariosRepository usuariosRepository;

    public TorneioEntity toEntity(TorneioDomain torneioDomain){
        TorneioEntity entity = new TorneioEntity();
        entity.setId(torneioDomain.id());
        entity.setNomeDoTorneio(torneioDomain.nomeDoTorneio());
        entity.setDescricaoDoTorneio(torneioDomain.descricaoDoTorneio());
        entity.setInicioDoTorneio(torneioDomain.inicioDoTorneio());
        entity.setJogoDoTorneio(torneioDomain.jogoDoTorneio());
        entity.setQuantidadeDeEquipes(torneioDomain.quantidadeDeEquipes());
        entity.setStatusDoTorneio(torneioDomain.statusDoTorneio());
        entity.setPlataforma(torneioDomain.plataforma());
        entity.setDataCriacao(torneioDomain.dataCriacao());

        if (torneioDomain.criadorId() != null) {
            UsuariosEntity criador = usuariosRepository.findById(torneioDomain.criadorId())
                    .orElseThrow(() -> new RuntimeException("Criador não encontrado com ID: " + torneioDomain.criadorId()));
            entity.setCriadorId(criador);
        }

        return entity;
    }

    public TorneioDomain toDomain(TorneioEntity torneioEntity){
        return new TorneioDomain(
                torneioEntity.getId(),
                torneioEntity.getNomeDoTorneio(),
                torneioEntity.getDescricaoDoTorneio(),
                torneioEntity.getInicioDoTorneio(),
                torneioEntity.getJogoDoTorneio(),
                torneioEntity.getQuantidadeDeEquipes(),
                torneioEntity.getCriadorId() != null ? torneioEntity.getCriadorId().getId() : null,
                torneioEntity.getStatusDoTorneio(),
                torneioEntity.getPlataforma(),
                torneioEntity.getDataCriacao()
        );
    }

}
