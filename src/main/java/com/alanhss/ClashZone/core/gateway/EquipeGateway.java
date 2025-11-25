package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.EquipeDomain;

import java.util.List;
import java.util.Optional;

public interface EquipeGateway {

    EquipeDomain criarEquipe(EquipeDomain equipeDomain);

    List<EquipeDomain> listarEquipes();

    EquipeDomain atualizarEquipe(Long id, EquipeDomain equipeDomain);

    Optional<EquipeDomain> buscarPorId(Long id);
}
