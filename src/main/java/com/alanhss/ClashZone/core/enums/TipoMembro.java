package com.alanhss.ClashZone.core.enums;

public enum TipoMembro {
    TITULAR("Titular"),
    RESERVA("Reserva");

    private final String descricao;

    TipoMembro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
