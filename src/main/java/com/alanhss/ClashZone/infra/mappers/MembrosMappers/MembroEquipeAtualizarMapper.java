package com.alanhss.ClashZone.infra.mappers.MembrosMappers;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.infra.dtos.MembrosDtos.AtualizarMembroDto;
import org.springframework.stereotype.Component;

@Component
public class MembroEquipeAtualizarMapper {

    public MembroEquipeDomain toDomain(Long id, Long equipeId, AtualizarMembroDto atualizarMembroDto) {
        return new MembroEquipeDomain(
                id,
                equipeId,
                atualizarMembroDto.nickname(),
                atualizarMembroDto.tipo(),
                atualizarMembroDto.rank(),
                null
        );
    }

    public AtualizarMembroDto validarEPreparar(AtualizarMembroDto atualizarMembroDto) {
        String nicknameNormalizado = atualizarMembroDto.nickname() != null
                ? atualizarMembroDto.nickname().trim()
                : null;

        String rankNormalizado = atualizarMembroDto.rank() != null
                ? atualizarMembroDto.rank().trim()
                : null;

        return new AtualizarMembroDto(
                atualizarMembroDto.id(),
                nicknameNormalizado,
                atualizarMembroDto.tipo(),
                rankNormalizado
        );
    }
}
