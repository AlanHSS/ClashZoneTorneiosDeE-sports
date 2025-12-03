package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;

import java.util.List;

public interface AdicionarMembrosEquipeUsecase {

    public List<MembroEquipeDomain> execute(Long equipeId, List<MembroEquipeDomain> membros, Long usuarioAutenticadoId, Role roleUsuario);
}
