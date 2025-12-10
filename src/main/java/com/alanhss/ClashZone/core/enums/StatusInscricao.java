package com.alanhss.ClashZone.core.enums;

public enum StatusInscricao {
    PENDENTE("Pendente de aprovação"),
    APROVADA("Inscrição aprovada"),
    RECUSADA("Inscrição recusada"),
    CANCELADA("Inscrição cancelada");

    private final String descricao;

    StatusInscricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
