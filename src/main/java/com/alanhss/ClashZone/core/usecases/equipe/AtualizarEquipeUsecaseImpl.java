package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

import java.util.ArrayList;
import java.util.List;

public class AtualizarEquipeUsecaseImpl implements AtualizarEquipeUsecase {

    private final EquipeGateway equipeGateway;

    public AtualizarEquipeUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public EquipeDomain execute(Long id, EquipeDomain equipeDomain, Long usuarioAutenticadoId, Role roleUsuario) {

        EquipeDomain equipeExistente = equipeGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipeExistente.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem modificá-la");
        }

        List<String> camposInvalidos = new ArrayList<>();

        if (equipeDomain.nomeDaEquipe() != null && equipeDomain.nomeDaEquipe().trim().isEmpty()) {
            camposInvalidos.add("Nome da equipe não pode estar vazio");
        }

        if (!camposInvalidos.isEmpty()) {
            throw new CampoObrigatorioException(camposInvalidos);
        }
        return equipeGateway.atualizarEquipe(id, equipeDomain);
    }
}
