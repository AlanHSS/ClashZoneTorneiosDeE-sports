package com.alanhss.ClashZone.infra.mappers.TorneiosMappers;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
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

    }public TorneioDto validarEPreparar(TorneioDto TorneioDto) {
        String nomeNormalizado = TorneioDto.nomeDoTorneio() != null
                ? TorneioDto.nomeDoTorneio().trim()
                : null;

        String descricaoNormalizada = TorneioDto.descricaoDoTorneio() != null
                ? TorneioDto.descricaoDoTorneio().trim()
                : null;

        return new TorneioDto(
                TorneioDto.id(),
                nomeNormalizado,
                descricaoNormalizada,
                TorneioDto.inicioDoTorneio(),
                TorneioDto.jogoDoTorneio(),
                TorneioDto.quantidadeDeEquipes(),
                TorneioDto.criadorDoTorneio(),
                TorneioDto.statusDoTorneio(),
                TorneioDto.plataforma(),
                TorneioDto.dataCriacao()
        );
    }
}
