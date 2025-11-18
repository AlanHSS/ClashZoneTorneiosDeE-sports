package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;

public interface AtualizarTorneioUsecase {

    public TorneioDomain execute(Long id, TorneioDomain torneioDomain, Long usuarioAutenticadoId);

}
