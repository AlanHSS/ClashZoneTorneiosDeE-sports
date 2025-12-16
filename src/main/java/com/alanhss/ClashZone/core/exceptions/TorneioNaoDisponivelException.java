package com.alanhss.ClashZone.core.exceptions;

import com.alanhss.ClashZone.core.enums.StatusDoTorneio;

public class TorneioNaoDisponivelException extends RuntimeException {
    private final Long torneioId;
    private final StatusDoTorneio statusAtual;

    public TorneioNaoDisponivelException(Long torneioId, StatusDoTorneio statusAtual) {
        super(String.format("Não é possível se inscrever no torneio ID %d. " +
                        "Inscrições só são permitidas em torneios com status AGENDADO. Status atual: %s",
                torneioId, statusAtual.name()));
        this.torneioId = torneioId;
        this.statusAtual = statusAtual;
    }

    public Long getTorneioId() {
        return torneioId;
    }

    public StatusDoTorneio getStatusAtual() {
        return statusAtual;
    }
}
