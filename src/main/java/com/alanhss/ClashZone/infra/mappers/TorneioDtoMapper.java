package com.alanhss.ClashZone.infra.mappers;

import com.alanhss.ClashZone.core.entities.Torneio;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import org.springframework.stereotype.Component;

@Component
public class TorneioDtoMapper {

    public TorneioDto toDto(Torneio torneio){
        return new TorneioDto(
                torneio.id(),
                torneio.nomeDoTorneio(),
                torneio.descricaoDoTorneio(),
                torneio.inicioDoTorneio(),
                torneio.jogoDoTorneio(),
                torneio.quantidadeDeEquipes(),
                torneio.criadorDoTorneio(),
                torneio.statusDoTorneio(),
                torneio.plataforma()
        );
    }

    public Torneio toEntity(TorneioDto torneioDto){
        return new Torneio(
                torneioDto.id(),
                torneioDto.nomeDoTorneio(),
                torneioDto.descricaoDoTorneio(),
                torneioDto.inicioDoTorneio(),
                torneioDto.jogoDoTorneio(),
                torneioDto.quantidadeDeEquipes(),
                torneioDto.criadorDoTorneio(),
                torneioDto.statusDoTorneio(),
                torneioDto.plataforma()
        );

    }
}
