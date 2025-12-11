package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioEntityMapper;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioEntity;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncricaoTorneioRepositoryGateway implements InscricaoTorneioGateway {

    private final InscricaoTorneioRepository inscricaoTorneioRepository;
    private final InscricaoTorneioEntityMapper mapper;

    @Override
    public InscricaoTorneioDomain criarInscricao(InscricaoTorneioDomain inscricaoTorneioDomain) {
        InscricaoTorneioEntity entity = mapper.toEntity(inscricaoTorneioDomain);
        InscricaoTorneioEntity novaInscricao = inscricaoTorneioRepository.save(entity);
        return mapper.toDomain(novaInscricao);
    }

    @Override
    public List<InscricaoTorneioDomain> listarInscricoesPorTorneio(Long torneioId) {
        List<InscricaoTorneioEntity> entities = inscricaoTorneioRepository.findByTorneioIdId(torneioId);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<InscricaoTorneioDomain> listarInscricoesPorEquipe(Long equipeId) {
        List<InscricaoTorneioEntity> entities = inscricaoTorneioRepository.findByEquipeIdId(equipeId);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<InscricaoTorneioDomain> listarInscricoesPorTorneioEStatus(Long torneioId, StatusInscricao status) {
        List<InscricaoTorneioEntity> entities = inscricaoTorneioRepository
                .findByTorneioIdIdAndStatusInscricao(torneioId, status);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeInscricao(Long torneioId, Long equipeId) {
        return inscricaoTorneioRepository.existsByTorneioIdIdAndEquipeIdId(torneioId, equipeId);
    }

    @Override
    public Optional<InscricaoTorneioDomain> buscarInscricao(Long torneioId, Long equipeId) {
        return inscricaoTorneioRepository.findByTorneioIdIdAndEquipeIdId(torneioId, equipeId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<InscricaoTorneioDomain> buscarPorId(Long id) {
        return inscricaoTorneioRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public int contarInscricoesAprovadas(Long torneioId) {
        return inscricaoTorneioRepository.contarInscricoesAprovadas(torneioId);
    }
}
