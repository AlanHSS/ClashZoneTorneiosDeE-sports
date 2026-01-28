package com.alanhss.ClashZone.core.exceptions;

public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }

    public AcessoNegadoException() {
        super("Você não tem permissão para realizar esta ação");
    }
}
