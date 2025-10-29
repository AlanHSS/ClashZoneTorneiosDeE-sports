package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;

import java.util.ArrayList;
import java.util.List;

public class AtualizarTorneioUsecaseImpl implements AtualizarTorneioUsecase{

    private final TorneioGateway torneioGateway;

    public AtualizarTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public TorneioDomain execute(Long id, TorneioDomain torneioDomain) {
        torneioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id));

        List<String> camposInvalidos = new ArrayList<>();

        if (torneioDomain.nomeDoTorneio() != null && torneioDomain.nomeDoTorneio().trim().isEmpty()) {
            camposInvalidos.add("nomeDoTorneio não pode estar vazio");
        }

        if (torneioDomain.quantidadeDeEquipes() != null && torneioDomain.quantidadeDeEquipes() <= 0) {
            camposInvalidos.add("quantidadeDeEquipes deve ser maior que zero");
        }

        if (torneioDomain.criadorDoTorneio() != null && torneioDomain.criadorDoTorneio().trim().isEmpty()) {
            camposInvalidos.add("criadorDoTorneio não pode estar vazio");
        }

        if (!camposInvalidos.isEmpty()) {
            throw new CampoObrigatorioException(camposInvalidos);
        }

        return torneioGateway.atualizarTorneio(id, torneioDomain);
    }

}
