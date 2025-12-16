package com.alanhss.ClashZone.core.exceptions;

import com.alanhss.ClashZone.core.enums.Games;

public class JogoIncompativelException extends RuntimeException {
    private final Games jogoEquipe;
    private final Games jogoTorneio;

    public JogoIncompativelException(Games jogoEquipe, Games jogoTorneio) {
        super(String.format("A equipe joga %s, mas o torneio é de %s",
                jogoEquipe.getNomeExibicao(),
                jogoTorneio.getNomeExibicao()));
        this.jogoEquipe = jogoEquipe;
        this.jogoTorneio = jogoTorneio;
    }

    public Games getJogoEquipe() {
        return jogoEquipe;
    }

    public Games getJogoTorneio() {
        return jogoTorneio;
    }
}
