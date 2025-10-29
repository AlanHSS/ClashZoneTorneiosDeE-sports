package com.alanhss.ClashZone.core.exceptions;

public class CampoDuplicadoException extends RuntimeException {
    private final String campo;
    private final String valor;

    public CampoDuplicadoException(String campo, String valor) {
        super("O " + campo + " '" + valor + "' já está em uso. Por favor, escolha outro.");
        this.campo = campo;
        this.valor = valor;
    }

    public String getCampo() {
        return campo;
    }

    public String getValor() {
        return valor;
    }
}
