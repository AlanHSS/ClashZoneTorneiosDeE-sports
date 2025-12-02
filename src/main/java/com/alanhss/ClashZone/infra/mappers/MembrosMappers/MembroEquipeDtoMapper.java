package com.alanhss.ClashZone.infra.mappers.MembrosMappers;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.infra.dtos.MembrosDtos.MembroEquipeDto;
import org.springframework.stereotype.Component;

@Component
public class MembroEquipeDtoMapper {

    public MembroEquipeDto toDto(MembroEquipeDomain membroEquipeDomain) {
        return new MembroEquipeDto(
                membroEquipeDomain.id(),
                membroEquipeDomain.equipeId(),
                membroEquipeDomain.nickname(),
                membroEquipeDomain.tipo(),
                membroEquipeDomain.rank(),
                membroEquipeDomain.dataAdicao()
        );
    }

    public MembroEquipeDomain toDomain(MembroEquipeDto membroEquipeDto) {
        return new MembroEquipeDomain(
                membroEquipeDto.id(),
                membroEquipeDto.equipeId(),
                membroEquipeDto.nickname(),
                membroEquipeDto.tipo(),
                membroEquipeDto.rank(),
                membroEquipeDto.dataAdicao()
        );
    }

    public MembroEquipeDto validarEPreparar(MembroEquipeDto membroEquipeDto) {
        String nicknameNormalizado = membroEquipeDto.nickname() != null
                ? membroEquipeDto.nickname().trim()
                : null;

        String rankNormalizado = membroEquipeDto.rank() != null
                ? membroEquipeDto.rank().trim()
                : null;

        return new MembroEquipeDto(
                membroEquipeDto.id(),
                membroEquipeDto.equipeId(),
                nicknameNormalizado,
                membroEquipeDto.tipo(),
                rankNormalizado,
                membroEquipeDto.dataAdicao()
        );
    }
}
