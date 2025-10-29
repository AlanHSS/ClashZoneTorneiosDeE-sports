package com.alanhss.ClashZone.core.exceptions;

public class NaoEncontradoPorIdException extends RuntimeException {

    public NaoEncontradoPorIdException(Long id) {
        super("Não foi encontrado nada com id: " + id);
    }

    public NaoEncontradoPorIdException(String mensagem) {
        super(mensagem);
    }
}
