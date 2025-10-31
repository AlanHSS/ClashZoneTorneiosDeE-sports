package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.DataInicioInvalidaException;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "torneio"));

        List<String> camposInvalidos = new ArrayList<>();

        if (torneioDomain.nomeDoTorneio() != null && torneioDomain.nomeDoTorneio().trim().isEmpty()) {
            camposInvalidos.add("Nome do torneio não pode estar vazio");
        }

        if (torneioDomain.quantidadeDeEquipes() != null && torneioDomain.quantidadeDeEquipes() <= 0) {
            camposInvalidos.add("Quantidade de equipes deve ser maior que zero");
        }

        if (torneioDomain.criadorDoTorneio() != null && torneioDomain.criadorDoTorneio().trim().isEmpty()) {
            camposInvalidos.add("Criador do torneio não pode estar vazio");
        }

        if (!camposInvalidos.isEmpty()) {
            throw new CampoObrigatorioException(camposInvalidos);
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataMinima = agora.plusDays(2);

        if (torneioDomain.inicioDoTorneio().isBefore(dataMinima)) {
            throw new DataInicioInvalidaException(torneioDomain.inicioDoTorneio(), dataMinima);
        }

        return torneioGateway.atualizarTorneio(id, torneioDomain);
    }

}
