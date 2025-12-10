package com.alanhss.ClashZone.infra.mappers.InscricaoMappers;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoTorneioDto;
import org.springframework.stereotype.Component;

@Component
public class InscricaoTorneioDtoMapper {
    public InscricaoTorneioDto toDto(InscricaoTorneioDomain domain) {
        return new InscricaoTorneioDto(
                domain.id(),
                domain.torneioId(),
                domain.equipeId(),
                domain.statusInscricao(),
                domain.motivoRecusa(),
                domain.dataInscricao()
        );
    }

    public InscricaoTorneioDomain toDomain(InscricaoTorneioDto dto) {
        return new InscricaoTorneioDomain(
                dto.id(),
                dto.torneioId(),
                dto.equipeId(),
                dto.statusInscricao(),
                dto.motivoRecusa(),
                dto.dataInscricao()
        );
    }
}
