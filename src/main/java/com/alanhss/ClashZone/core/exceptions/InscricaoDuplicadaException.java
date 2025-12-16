package com.alanhss.ClashZone.core.exceptions;

public class InscricaoDuplicadaException extends RuntimeException {
    private final Long torneioId;
    private final Long equipeId;

    public InscricaoDuplicadaException(Long torneioId, Long equipeId) {
        super(String.format("A equipe ID %d já possui uma inscrição no torneio ID %d", equipeId, torneioId));
        this.torneioId = torneioId;
        this.equipeId = equipeId;
    }

    public Long getTorneioId() {
        return torneioId;
    }

    public Long getEquipeId() {
        return equipeId;
    }
}
