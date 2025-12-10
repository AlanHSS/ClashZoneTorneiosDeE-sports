package com.alanhss.ClashZone.infra.mappers.InscricaoMappers;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.AtualizarInscricaoDto;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoTorneioDto;
import org.springframework.stereotype.Component;

@Component
public class InscricaoTorneioAtualizarMapper {

    public InscricaoTorneioDomain toDomainFromAtualizacao(Long id, Long torneioId, Long equipeId, AtualizarInscricaoDto dto) {
        return new InscricaoTorneioDomain(
                id,
                torneioId,
                equipeId,
                dto.statusInscricao(),
                dto.motivoRecusa(),
                null
        );
    }

    public InscricaoTorneioDto validarEPreparar(InscricaoTorneioDto dto) {
        String motivoNormalizado = dto.motivoRecusa() != null
                ? dto.motivoRecusa().trim()
                : null;

        return new InscricaoTorneioDto(
                dto.id(),
                dto.torneioId(),
                dto.equipeId(),
                dto.statusInscricao(),
                motivoNormalizado,
                dto.dataInscricao()
        );
    }
}
