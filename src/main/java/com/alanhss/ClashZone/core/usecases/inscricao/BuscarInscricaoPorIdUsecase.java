package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;

public interface BuscarInscricaoPorIdUsecase {

    InscricaoTorneioDomain execute(Long id);
}
