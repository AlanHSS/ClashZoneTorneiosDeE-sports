package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;

import java.util.List;
import java.util.stream.Collectors;

public class ListarInscricoesPorEquipeUsecaseImpl implements ListarInscricoesPorEquipeUsecase{

    private final InscricaoTorneioGateway inscricaoTorneioGateway;
    private final EquipeGateway equipeGateway;

    public ListarInscricoesPorEquipeUsecaseImpl(InscricaoTorneioGateway inscricaoTorneioGateway, EquipeGateway equipeGateway) {
        this.inscricaoTorneioGateway = inscricaoTorneioGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<InscricaoTorneioDomain> execute(Long equipeId, StatusInscricao statusFiltro, Long usuarioAutenticadoId, Role roleUsuario) {

        EquipeDomain equipe = equipeGateway.buscarPorId(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipe.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem visualizar o histórico de inscrições");
        }

        List<InscricaoTorneioDomain> inscricoes = inscricaoTorneioGateway.listarInscricoesPorEquipe(equipeId);

        if (statusFiltro != null) {
            inscricoes = inscricoes.stream()
                    .filter(inscricao -> inscricao.statusInscricao() == statusFiltro)
                    .collect(Collectors.toList());
        }

        return inscricoes;
    }

}
