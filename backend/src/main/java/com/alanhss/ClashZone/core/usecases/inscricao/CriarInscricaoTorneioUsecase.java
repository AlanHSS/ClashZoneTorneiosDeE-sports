package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;

public interface CriarInscricaoTorneioUsecase {

    InscricaoTorneioDomain execute(InscricaoTorneioDomain inscricaoTorneioDomain, Long usuarioAutenticadoId);
}
