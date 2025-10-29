package com.alanhss.ClashZone.core.exceptions;

public class NicknameJaExisteException extends RuntimeException{

    public NicknameJaExisteException(String nickname) {
        super("O nickname '" + nickname + "' já está em uso. Por favor, escolha outro.");
    }
}
