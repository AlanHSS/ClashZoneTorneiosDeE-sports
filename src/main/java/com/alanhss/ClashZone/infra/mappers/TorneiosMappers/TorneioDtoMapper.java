package com.alanhss.ClashZone.infra.mappers.TorneiosMappers;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import org.springframework.stereotype.Component;

@Component
public class TorneioDtoMapper {

    public TorneioDto toDto(TorneioDomain torneioDomain){
        return new TorneioDto(
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

    public TorneioDomain toDomain(TorneioDto torneioDto){
        return new TorneioDomain(
                torneioDto.id(),
                torneioDto.nomeDoTorneio(),
                torneioDto.descricaoDoTorneio(),
                torneioDto.inicioDoTorneio(),
                torneioDto.jogoDoTorneio(),
                torneioDto.quantidadeDeEquipes(),
                torneioDto.criadorDoTorneio(),
                torneioDto.statusDoTorneio(),
                torneioDto.plataforma(),
                torneioDto.dataCriacao()
        );

    }
}
