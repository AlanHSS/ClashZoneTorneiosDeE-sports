package com.alanhss.ClashZone.infra.mappers;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import org.springframework.stereotype.Component;

@Component
public class TorneioEntityMapper {

    public TorneioEntity toEntity(TorneioDomain torneioDomain){
        return new TorneioEntity(
                torneioDomain.id(),
                torneioDomain.nomeDoTorneio(),
                torneioDomain.descricaoDoTorneio(),
                torneioDomain.inicioDoTorneio(),
                torneioDomain.jogoDoTorneio(),
                torneioDomain.quantidadeDeEquipes(),
                torneioDomain.criadorDoTorneio(),
                torneioDomain.statusDoTorneio(),
                torneioDomain.plataforma(),
                torneioDomain.dataCriacao()
        );
    }

    public TorneioDomain toDomain(TorneioEntity torneioEntity){
        return new TorneioDomain(
                torneioEntity.getId(),
                torneioEntity.getNomeDoTorneio(),
                torneioEntity.getDescricaoDoTorneio(),
                torneioEntity.getInicioDoTorneio(),
                torneioEntity.getJogoDoTorneio(),
                torneioEntity.getQuantidadeDeEquipes(),
                torneioEntity.getCriadorDoTorneio(),
                torneioEntity.getStatusDoTorneio(),
                torneioEntity.getPlataforma(),
                torneioEntity.getDataCriacao()
        );
    }

}
