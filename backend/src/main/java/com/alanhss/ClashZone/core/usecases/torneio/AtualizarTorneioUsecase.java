package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;

public interface AtualizarTorneioUsecase {

    public TorneioDomain execute(Long id, TorneioDomain torneioDomain, Long usuarioAutenticadoId, Role roleUsuario);

}
