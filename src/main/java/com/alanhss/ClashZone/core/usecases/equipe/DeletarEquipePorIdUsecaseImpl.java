package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;

public class DeletarEquipePorIdUsecaseImpl implements DeletarEquipePorIdUsecase{

    private final EquipeGateway equipeGateway;

    public DeletarEquipePorIdUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public void execute(Long id, Long usuarioAutenticadoId, Role roleUsuario) {
        EquipeDomain equipeExistente = equipeGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipeExistente.liderId().equals(usuarioAutenticadoId)){
            throw new AcessoNegadoException("Você só pode deletar sua própria equipe");
        }

        equipeGateway.deletarEquipe(id);
    }
}
