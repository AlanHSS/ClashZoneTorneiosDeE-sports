package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;

public class BuscarInscricaoPorIdUsecaseImpl implements BuscarInscricaoPorIdUsecase {

    private final InscricaoTorneioGateway inscricaoTorneioGateway;

    public BuscarInscricaoPorIdUsecaseImpl(InscricaoTorneioGateway inscricaoTorneioGateway) {
        this.inscricaoTorneioGateway = inscricaoTorneioGateway;
    }

    @Override
    public InscricaoTorneioDomain execute(Long id) {
        return inscricaoTorneioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "inscrição"));
    }
}
