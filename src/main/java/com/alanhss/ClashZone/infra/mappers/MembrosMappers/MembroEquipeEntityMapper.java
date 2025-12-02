package com.alanhss.ClashZone.infra.mappers.MembrosMappers;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.infra.persistence.MembrosPersistence.MembroEquipeEntity;
import org.springframework.stereotype.Component;

@Component
public class MembroEquipeEntityMapper {

    public MembroEquipeEntity toEntity(MembroEquipeDomain membroEquipeDomain) {
        return new MembroEquipeEntity(
                membroEquipeDomain.id(),
                membroEquipeDomain.equipeId(),
                membroEquipeDomain.nickname(),
                membroEquipeDomain.tipo(),
                membroEquipeDomain.rank(),
                membroEquipeDomain.dataAdicao()
        );
    }

    public MembroEquipeDomain toDomain(MembroEquipeEntity membroEquipeEntity) {
        return new MembroEquipeDomain(
                membroEquipeEntity.getId(),
                membroEquipeEntity.getEquipeId(),
                membroEquipeEntity.getNickname(),
                membroEquipeEntity.getTipo(),
                membroEquipeEntity.getRank(),
                membroEquipeEntity.getDataAdicao()
        );
    }
}
