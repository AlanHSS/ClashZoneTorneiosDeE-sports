package com.alanhss.ClashZone.infra.mappers.TorneiosMappers;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;
import org.springframework.stereotype.Component;

@Component
public class TorneioFiltroMapper {

    public TorneioDomain toDomain(FiltroTorneioDto filtroTorneioDto) {
        return new TorneioDomain(
                null,
                filtroTorneioDto.nomeDoTorneio(),
                null,
                filtroTorneioDto.inicioDoTorneio(),
                filtroTorneioDto.jogoDoTorneio(),
                null,
                null,
                filtroTorneioDto.statusDoTorneio(),
                filtroTorneioDto.plataforma(),
                null
        );
    }

    public FiltroTorneioDto validarEPrepararFiltro(FiltroTorneioDto filtroTorneioDto) {
        String nomeNormalizado = filtroTorneioDto.nomeDoTorneio() != null
                ? filtroTorneioDto.nomeDoTorneio().trim()
                : null;

        return new FiltroTorneioDto(
                nomeNormalizado,
                filtroTorneioDto.inicioDoTorneio(),
                filtroTorneioDto.jogoDoTorneio(),
                filtroTorneioDto.statusDoTorneio(),
                filtroTorneioDto.plataforma()
        );
    }
}
