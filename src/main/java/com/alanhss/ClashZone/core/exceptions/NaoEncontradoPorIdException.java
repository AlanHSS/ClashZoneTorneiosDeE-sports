package com.alanhss.ClashZone.core.exceptions;

public class NaoEncontradoPorIdException extends RuntimeException {

    private final String campo;

    public NaoEncontradoPorIdException(Long id, String campo) {
        super("Não foi encontrado nenhum " + campo + " com id: " + id);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
