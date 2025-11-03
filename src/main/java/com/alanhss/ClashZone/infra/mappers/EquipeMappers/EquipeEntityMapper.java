package com.alanhss.ClashZone.infra.mappers.EquipeMappers;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;
import org.springframework.stereotype.Component;

@Component
public class EquipeEntityMapper {

    public EquipeEntity toEntity(EquipeDomain equipeDomain) {
        return new EquipeEntity(
                equipeDomain.id(),
                equipeDomain.nomeDaEquipe(),
                equipeDomain.liderId(),
                equipeDomain.jogo(),
                equipeDomain.dataCriacao(),
                equipeDomain.inscrita()
        );
    }

    public EquipeDomain toDomain(EquipeEntity equipeEntity) {
        return new EquipeDomain(
                equipeEntity.getId(),
                equipeEntity.getNomeDaEquipe(),
                equipeEntity.getLiderId(),
                equipeEntity.getJogo(),
                equipeEntity.getDataCriacao(),
                equipeEntity.isInscrita()
        );
    }
}
