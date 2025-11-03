package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.EquipeDomain;

import java.util.List;

public interface EquipeGateway {

    EquipeDomain criarEquipe(EquipeDomain equipeDomain);

    List<EquipeDomain> listarEquipes();
}
