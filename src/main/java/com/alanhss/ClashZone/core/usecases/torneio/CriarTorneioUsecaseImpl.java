package com.alanhss.ClashZone.core.usecases.torneio;
import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.ArrayList;
import java.util.List;

public class CriarTorneioUsecaseImpl implements CriarTorneioUsecase {

    private final TorneioGateway torneioGateway;

    public CriarTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public TorneioDomain execute(TorneioDomain torneioDomain) {

        List<String> camposFaltantes = new ArrayList<>();

        if (torneioDomain.nomeDoTorneio() == null || torneioDomain.nomeDoTorneio().trim().isEmpty()) {
            camposFaltantes.add("nomeDoTorneio");
        }

        if (torneioDomain.inicioDoTorneio() == null) {
            camposFaltantes.add("inicioDoTorneio");
        }

        if (torneioDomain.jogoDoTorneio() == null) {
            camposFaltantes.add("jogoDoTorneio");
        }

        if (torneioDomain.quantidadeDeEquipes() == null || torneioDomain.quantidadeDeEquipes() <= 0) {
            camposFaltantes.add("quantidadeDeEquipes");
        }

        if (torneioDomain.criadorDoTorneio() == null) {
            camposFaltantes.add("criadorDoTorneio");
        }

        if (torneioDomain.statusDoTorneio() == null) {
            camposFaltantes.add("statusDoTorneio");
        }

        if (torneioDomain.plataforma() == null) {
            camposFaltantes.add("plataforma");
        }

        // Se houver campos faltantes, lançar exception
        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        return torneioGateway.criarTorneio(torneioDomain);
    }
}
