package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.List;

public class ListarInscricoesPorTorneioUsecaseImpl implements ListarInscricoesPorTorneioUsecase {

    private final InscricaoTorneioGateway inscricaoTorneioGateway;
    private final TorneioGateway torneioGateway;

    public ListarInscricoesPorTorneioUsecaseImpl(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway) {
        this.inscricaoTorneioGateway = inscricaoTorneioGateway;
        this.torneioGateway = torneioGateway;
    }

    @Override
    public List<InscricaoTorneioDomain> execute(Long torneioId, StatusInscricao statusFiltro, Long usuarioAutenticadoId, Role roleUsuario) {

        TorneioDomain torneioExistente = torneioGateway.buscarPorId(torneioId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(torneioId, "torneio"));


        if (roleUsuario != Role.ADMIN && !torneioExistente.criadorId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o criador do torneio ou um administrador podem visualizar as inscrições");
        }

        if (statusFiltro != null) {
            return inscricaoTorneioGateway.listarInscricoesPorTorneioEStatus(torneioId, statusFiltro);
        }

        return inscricaoTorneioGateway.listarInscricoesPorTorneio(torneioId);
    }
}
