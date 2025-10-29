package com.alanhss.ClashZone.core.exceptions;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String emailDoUsuario) {
        super("O email '" + emailDoUsuario + "' já está em uso. Por favor, escolha outro ou faça login com este email");
    }
}
