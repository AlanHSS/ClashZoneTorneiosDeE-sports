package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;

import java.util.List;

public interface ListarInscricoesPorTorneioUsecase {

    List<InscricaoTorneioDomain> execute(Long torneioId, StatusInscricao statusFiltro, Long usuarioAutenticadoId, Role roleUsuario);
}
