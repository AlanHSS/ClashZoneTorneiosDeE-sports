package com.alanhss.ClashZone.infra.mappers.EquipeMappers;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import org.springframework.stereotype.Component;

@Component
public class EquipeDtoMapper {
    public EquipeDto toDto(EquipeDomain equipeDomain){
        return new EquipeDto(
                equipeDomain.id(),
                equipeDomain.nomeDaEquipe(),
                equipeDomain.liderId(),
                equipeDomain.jogo(),
                equipeDomain.dataCriacao(),
                equipeDomain.inscrita()
        );
    }

    public EquipeDomain toDomain(EquipeDto equipeDto){
        return new EquipeDomain(
                equipeDto.id(),
                equipeDto.nomeDaEquipe(),
                equipeDto.liderId(),
                equipeDto.jogo(),
                equipeDto.dataCriacao(),
                equipeDto.inscrita()
        );
    }

    public EquipeDto validarEPreparar(EquipeDto equipeDto){
        String nomeNormalizado = equipeDto.nomeDaEquipe() != null
                ? equipeDto.nomeDaEquipe().trim()
                : null;

        return new EquipeDto(
                equipeDto.id(),
                nomeNormalizado,
                equipeDto.liderId(),
                equipeDto.jogo(),
                equipeDto.dataCriacao(),
                equipeDto.inscrita()
        );
    }

}
