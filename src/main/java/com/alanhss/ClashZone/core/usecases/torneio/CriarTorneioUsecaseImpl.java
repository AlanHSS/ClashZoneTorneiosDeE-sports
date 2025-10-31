package com.alanhss.ClashZone.core.usecases.torneio;
import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.DataInicioInvalidaException;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.time.LocalDateTime;
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
            camposFaltantes.add("Nome do torneio");
        }

        if (torneioDomain.inicioDoTorneio() == null) {
            camposFaltantes.add("Data de inicio");
        }

        if (torneioDomain.jogoDoTorneio() == null) {
            camposFaltantes.add("Jogo do torneio");
        }

        if (torneioDomain.quantidadeDeEquipes() == null || torneioDomain.quantidadeDeEquipes() <= 0) {
            camposFaltantes.add("Quantidade de equipes");
        }

        if (torneioDomain.criadorDoTorneio() == null) {
            camposFaltantes.add("Criador do torneio");
        }

        if (torneioDomain.statusDoTorneio() == null) {
            camposFaltantes.add("Status do torneio");
        }

        if (torneioDomain.plataforma() == null) {
            camposFaltantes.add("Plataforma");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataMinima = agora.plusDays(2);

        if (torneioDomain.inicioDoTorneio().isBefore(dataMinima)) {
            throw new DataInicioInvalidaException(torneioDomain.inicioDoTorneio(), dataMinima);
        }

        return torneioGateway.criarTorneio(torneioDomain);
    }
}
