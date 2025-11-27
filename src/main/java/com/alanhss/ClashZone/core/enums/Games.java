package com.alanhss.ClashZone.core.enums;

public enum Games {
    LEAGUE_OF_LEGENDS("League of Legends", 5, 2, true),
    CS2("Counter-Strike 2", 5, 2, true),
    DOTA2("Dota 2", 5, 2, true),
    VALORANT("Valorant", 5, 2, true),
    OVERWATCH_2("Overwatch 2", 5, 3, true),
    RAINBOW_SIX_SIEGE("Rainbow Six Siege", 5, 2, true),
    FIFA("FIFA", 1, 0, false),
    STREET_FIGHTERS_6("Street Fighter 6", 1, 0, false),
    TEKKEN_8("Tekken 8", 1, 0, false),
    FORTNITE("Fortnite", 4, 1, true),
    PUBG("PUBG", 4, 1, true),
    OUTRO("Outro", 1, 0, false);

    private final String nomeExibicao;
    private final int jogadoresTitulares;
    private final int jogadoresReservas;
    private final boolean permiteReservas;

    Games(String nomeExibicao, int jogadoresTitulares, int jogadoresReservas, boolean permiteReservas) {
        this.nomeExibicao = nomeExibicao;
        this.jogadoresTitulares = jogadoresTitulares;
        this.jogadoresReservas = jogadoresReservas;
        this.permiteReservas = permiteReservas;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public int getJogadoresTitulares() {
        return jogadoresTitulares;
    }

    public int getJogadoresReservas() {
        return jogadoresReservas;
    }

    public boolean isPermiteReservas() {
        return permiteReservas;
    }

    public int getTotalMaximoJogadores() {
        return jogadoresTitulares + jogadoresReservas;
    }

    public int getMinimoJogadores() {
        return jogadoresTitulares;
    }
}
