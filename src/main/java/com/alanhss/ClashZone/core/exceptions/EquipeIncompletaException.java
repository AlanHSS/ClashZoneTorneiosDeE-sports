package com.alanhss.ClashZone.core.exceptions;

import com.alanhss.ClashZone.core.enums.Games;

public class EquipeIncompletaException extends RuntimeException {
    private final Long equipeId;
    private final int jogadoresAtuais;
    private final int jogadoresMinimos;
    private final Games jogo;

    public EquipeIncompletaException(Long equipeId, int jogadoresAtuais, int jogadoresMinimos, Games jogo) {
        super(String.format("A equipe ID %d precisa ter pelo menos %d jogadores titulares para %s. " +
                        "Atualmente possui: %d",
                equipeId, jogadoresMinimos, jogo.getNomeExibicao(), jogadoresAtuais));
        this.equipeId = equipeId;
        this.jogadoresAtuais = jogadoresAtuais;
        this.jogadoresMinimos = jogadoresMinimos;
        this.jogo = jogo;
    }

    public Long getEquipeId() {
        return equipeId;
    }

    public int getJogadoresAtuais() {
        return jogadoresAtuais;
    }

    public int getJogadoresMinimos() {
        return jogadoresMinimos;
    }

    public Games getJogo() {
        return jogo;
    }
}
