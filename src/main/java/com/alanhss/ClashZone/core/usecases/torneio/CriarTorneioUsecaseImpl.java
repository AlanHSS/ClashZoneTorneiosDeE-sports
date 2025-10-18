package com.alanhss.ClashZone.core.usecases.torneio;
import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

public class CriarTorneioUsecaseImpl implements CriarTorneioUsecase {

    private final TorneioGateway torneioGateway;

    public CriarTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public TorneioDomain execute(TorneioDomain torneioDomain) {
        return torneioGateway.criarTorneio(torneioDomain);
    }
}
