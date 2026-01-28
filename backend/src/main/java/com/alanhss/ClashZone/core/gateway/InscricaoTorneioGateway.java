package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.StatusInscricao;

import java.util.List;
import java.util.Optional;

public interface InscricaoTorneioGateway {

    InscricaoTorneioDomain criarInscricao(InscricaoTorneioDomain inscricaoTorneioDomain);

    List<InscricaoTorneioDomain> listarInscricoesPorTorneio(Long torneioId);

    List<InscricaoTorneioDomain> listarInscricoesPorEquipe(Long equipeId);

    List<InscricaoTorneioDomain> listarInscricoesPorTorneioEStatus(Long torneioId, StatusInscricao status);

    boolean existeInscricao(Long torneioId, Long equipeId);

    Optional<InscricaoTorneioDomain> buscarInscricao(Long torneioId, Long equipeId);

    Optional<InscricaoTorneioDomain> buscarPorId(Long id);

    int contarInscricoesAprovadas(Long torneioId);

    InscricaoTorneioDomain atualizarInscricao(Long id, InscricaoTorneioDomain inscricaoTorneioDomain);
}
