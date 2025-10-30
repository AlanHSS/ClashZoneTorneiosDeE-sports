package com.alanhss.ClashZone.core.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataInicioInvalidaException extends RuntimeException {
    private final LocalDateTime dataInformada;
    private final LocalDateTime dataMinima;

    public DataInicioInvalidaException(LocalDateTime dataInformada, LocalDateTime dataMinima) {
        super(String.format("A data de início do torneio deve ser pelo menos 2 dias após a data atual. " +
                        "Data informada: %s. Data mínima permitida: %s",
                formatarData(dataInformada),
                formatarData(dataMinima)
        ));
        this.dataInformada = dataInformada;
        this.dataMinima = dataMinima;
    }

    public DataInicioInvalidaException(String mensagem) {
        super(mensagem);
        this.dataInformada = null;
        this.dataMinima = null;
    }

    private static String formatarData(LocalDateTime data) {
        if (data == null) return "não informada";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }

    public LocalDateTime getDataInformada() {
        return dataInformada;
    }

    public LocalDateTime getDataMinima() {
        return dataMinima;
    }
}
