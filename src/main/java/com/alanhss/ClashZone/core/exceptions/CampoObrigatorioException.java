package com.alanhss.ClashZone.core.exceptions;

import java.util.List;

public class CampoObrigatorioException extends RuntimeException {
    private final List<String> camposFaltantes;

    public CampoObrigatorioException(String campo) {
        super("O campo '" + campo + "' é obrigatório e não pode estar vazio");
        this.camposFaltantes = List.of(campo);
    }

    public CampoObrigatorioException(List<String> campos) {
        super("Os seguintes campos são obrigatórios e não podem estar vazios: " + String.join(", ", campos));
        this.camposFaltantes = campos;
    }

    public List<String> getCamposFaltantes() {
        return camposFaltantes;
    }
}
