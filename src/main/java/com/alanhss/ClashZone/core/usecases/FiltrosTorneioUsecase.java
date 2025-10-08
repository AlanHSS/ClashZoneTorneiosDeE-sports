package com.alanhss.ClashZone.core.usecases;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;

import java.util.List;

public interface FiltrosTorneioUsecase {

    public List<TorneioDomain> execute(FiltroTorneioDto filtroTorneioDto);
}
