package com.alanhss.ClashZone.infra.exceptions;

public class NaoEncontradoPorIdException extends RuntimeException {

    public NaoEncontradoPorIdException(Long id) {
        super("Não foi encontrado nada com id: " + id);
    }

    public NaoEncontradoPorIdException(String mensagem) {
        super(mensagem);
    }
}
