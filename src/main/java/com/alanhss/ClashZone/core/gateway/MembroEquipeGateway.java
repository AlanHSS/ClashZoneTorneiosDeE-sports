package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;

import java.util.List;
import java.util.Optional;

public interface MembroEquipeGateway {

    List<MembroEquipeDomain> adicionarMembros(List<MembroEquipeDomain> membros);

    List<MembroEquipeDomain> buscarMembrosPorEquipe(Long equipeId);

    int contarTitularesPorEquipe(Long equipeId);

    int contarReservasPorEquipe(Long equipeId);

    Optional<MembroEquipeDomain> buscarPorId(Long id);

    void deletarMembroEquipe(Long id);

    MembroEquipeDomain atualizarMembros(Long id, MembroEquipeDomain membros);
}
