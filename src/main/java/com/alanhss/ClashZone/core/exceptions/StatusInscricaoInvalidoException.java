package com.alanhss.ClashZone.core.exceptions;

import com.alanhss.ClashZone.core.enums.StatusInscricao;

public class StatusInscricaoInvalidoException extends RuntimeException {
    private final StatusInscricao statusAtual;
    private final StatusInscricao novoStatus;

    public StatusInscricaoInvalidoException(StatusInscricao statusAtual, StatusInscricao novoStatus) {
        super(String.format("Não é possível alterar o status da inscrição de %s para %s",
                statusAtual.getDescricao(),
                novoStatus.getDescricao()));
        this.statusAtual = statusAtual;
        this.novoStatus = novoStatus;
    }

    public StatusInscricao getStatusAtual() {
        return statusAtual;
    }

    public StatusInscricao getNovoStatus() {
        return novoStatus;
    }
}
