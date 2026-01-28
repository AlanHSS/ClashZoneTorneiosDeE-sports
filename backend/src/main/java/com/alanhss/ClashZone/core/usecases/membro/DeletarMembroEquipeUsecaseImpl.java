package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.DadoInvalidoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;

public class DeletarMembroEquipeUsecaseImpl implements DeletarMembroEquipeUsecase{

    private final MembroEquipeGateway membroEquipeGateway;
    private final EquipeGateway equipeGateway;

    public DeletarMembroEquipeUsecaseImpl(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        this.membroEquipeGateway = membroEquipeGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public void execute(Long equipeId, Long membroId, Long usuarioAutenticadoId, Role roleUsuario) {
        EquipeDomain equipe = equipeGateway.buscarPorId(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipe.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem remover membros");
        }

        MembroEquipeDomain membro = membroEquipeGateway.buscarPorId(membroId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(membroId, "membro"));

        if (!membro.equipeId().equals(equipeId)) {
            throw new DadoInvalidoException("membro", "Este membro não pertence à equipe especificada");
        }

        membroEquipeGateway.deletarMembroEquipe(membroId);
    }
}
