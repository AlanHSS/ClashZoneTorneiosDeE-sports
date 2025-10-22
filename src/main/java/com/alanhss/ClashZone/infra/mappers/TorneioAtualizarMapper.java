package com.alanhss.ClashZone.infra.mappers;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.infra.dtos.AtualizarTorneioDto;
import org.springframework.stereotype.Component;

@Component
public class TorneioAtualizarMapper {

    public TorneioDomain toDomain(AtualizarTorneioDto atualizarTorneioDto, Long id) {
        return new TorneioDomain(
                id,
                atualizarTorneioDto.nomeDoTorneio(),
                atualizarTorneioDto.descricaoDoTorneio(),
                atualizarTorneioDto.inicioDoTorneio(),
                atualizarTorneioDto.jogoDoTorneio(),
                atualizarTorneioDto.quantidadeDeEquipes(),
                null,
                atualizarTorneioDto.statusDoTorneio(),
                atualizarTorneioDto.plataforma(),
                null
        );
    }

    public AtualizarTorneioDto validarEPrepararAtualizacao(AtualizarTorneioDto atualizarTorneioDto) {
        String nomeNormalizado = atualizarTorneioDto.nomeDoTorneio() != null
                ? atualizarTorneioDto.nomeDoTorneio().trim()
                : null;

        String descricaoNormalizada = atualizarTorneioDto.descricaoDoTorneio() != null
                ? atualizarTorneioDto.descricaoDoTorneio().trim()
                : null;

        return new AtualizarTorneioDto(
                nomeNormalizado,
                descricaoNormalizada,
                atualizarTorneioDto.inicioDoTorneio(),
                atualizarTorneioDto.jogoDoTorneio(),
                atualizarTorneioDto.quantidadeDeEquipes(),
                atualizarTorneioDto.statusDoTorneio(),
                atualizarTorneioDto.plataforma()
        );
    }

}
