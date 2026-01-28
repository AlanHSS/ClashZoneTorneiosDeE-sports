package com.alanhss.ClashZone.infra.mappers.EquipeMappers;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.AtualizarEquipeDto;
import org.springframework.stereotype.Component;

@Component
public class EquipeAtualizarMapper {

    public EquipeDomain toDomain(Long id, AtualizarEquipeDto atualizarEquipeDto){
        return new EquipeDomain(
                id,
                atualizarEquipeDto.nomeDaEquipe(),
                null,
                atualizarEquipeDto.jogo(),
                null,
                atualizarEquipeDto.inscrita()
        );
    }

    public AtualizarEquipeDto validarEPreparar(AtualizarEquipeDto atualizarEquipeDto){
        String nomeNormalizado = atualizarEquipeDto.nomeDaEquipe() != null
                ? atualizarEquipeDto.nomeDaEquipe().trim()
                : null;

        return new AtualizarEquipeDto(
                nomeNormalizado,
                atualizarEquipeDto.jogo(),
                atualizarEquipeDto.inscrita()
        );
    }
}
