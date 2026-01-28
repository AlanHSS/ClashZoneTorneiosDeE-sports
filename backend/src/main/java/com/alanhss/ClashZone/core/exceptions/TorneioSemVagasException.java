package com.alanhss.ClashZone.core.exceptions;

public class TorneioSemVagasException extends RuntimeException {
    private final Long torneioId;
    private final int vagasOcupadas;
    private final int vagasTotais;

    public TorneioSemVagasException(Long torneioId, int vagasOcupadas, int vagasTotais) {
        super(String.format("O torneio ID %d já atingiu o limite de equipes (%d/%d)",
                torneioId, vagasOcupadas, vagasTotais));
        this.torneioId = torneioId;
        this.vagasOcupadas = vagasOcupadas;
        this.vagasTotais = vagasTotais;
    }

    public Long getTorneioId() {
        return torneioId;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    public int getVagasTotais() {
        return vagasTotais;
    }
}
