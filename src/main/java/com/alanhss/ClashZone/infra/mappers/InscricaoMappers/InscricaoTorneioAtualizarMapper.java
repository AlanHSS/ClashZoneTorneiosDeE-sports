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

    public AtualizarInscricaoDto validarEPreparar(AtualizarInscricaoDto dto) {
        String motivoNormalizado = dto.motivoRecusa() != null
                ? dto.motivoRecusa().trim()
                : null;

        return new AtualizarInscricaoDto(
                dto.statusInscricao(),
                motivoNormalizado
        );
    }
}
