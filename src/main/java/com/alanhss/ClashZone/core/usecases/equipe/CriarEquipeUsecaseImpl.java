package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

import java.util.ArrayList;
import java.util.List;

public class CriarEquipeUsecaseImpl implements CriarEquipeUsecase {

    private final EquipeGateway equipeGateway;

    public CriarEquipeUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public EquipeDomain execute(EquipeDomain equipeDomain) {

        List<String> camposFaltantes = new ArrayList<>();

        if (equipeDomain.nomeDaEquipe() == null || equipeDomain.nomeDaEquipe().trim().isEmpty()) {
            camposFaltantes.add("Nome da equipe");
        }

        if (equipeDomain.nomeDaEquipe() == null) {
            camposFaltantes.add("Jogo da equipe");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        return equipeGateway.criarEquipe(equipeDomain);
    }
}
