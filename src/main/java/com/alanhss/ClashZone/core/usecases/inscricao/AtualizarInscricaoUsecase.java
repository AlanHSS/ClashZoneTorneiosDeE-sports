package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;

public interface AtualizarInscricaoUsecase {

    InscricaoTorneioDomain execute(Long id, InscricaoTorneioDomain inscricaoTorneioDomain, Long usuarioAutenticadoId, Role roleUsuario);
}
