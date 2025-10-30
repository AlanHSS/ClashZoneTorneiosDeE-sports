package com.alanhss.ClashZone.core.exceptions;

import java.util.List;

public class DadoInvalidoException extends RuntimeException {
    private final String campo;
    private final String valorInvalido;

    public DadoInvalidoException(String campo, String valorInvalido) {
        super("O valor '" + valorInvalido + "' não é válido para o campo '" + campo +"'");
        this.campo = campo;
        this.valorInvalido = valorInvalido;
    }

    public String getCampo() {
        return campo;
    }

    public String getValorInvalido() {
        return valorInvalido;
    }
}