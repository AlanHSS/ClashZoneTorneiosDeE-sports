package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;

import java.util.List;

public interface ListarMembrosPorEquipeUsecase {

    public List<MembroEquipeDomain> execute(Long equipeId);
}
