package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

public class BuscarEquipePorIdUsecaseImpl implements BuscarEquipePorIdUsecase{
    
    private final EquipeGateway equipeGateway;

    public BuscarEquipePorIdUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public EquipeDomain execute(Long id, Long usuarioAutenticadoId, Role roleUsuario) {

        EquipeDomain equipeExistente = equipeGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipeExistente.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem visualiza-la");
        }

        return equipeExistente;
    }
}
