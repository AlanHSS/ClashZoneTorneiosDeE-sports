package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.TipoMembro;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeEntityMapper;
import com.alanhss.ClashZone.infra.persistence.MembrosPersistence.MembroEquipeEntity;
import com.alanhss.ClashZone.infra.persistence.MembrosPersistence.MembroEquipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MembroEquipeRepositoryGateway implements MembroEquipeGateway {

    private final MembroEquipeRepository membroEquipeRepository;
    private final MembroEquipeEntityMapper mapper;

    @Override
    public List<MembroEquipeDomain> adicionarMembros(List<MembroEquipeDomain> membros) {
        List<MembroEquipeEntity> entities = membros.stream()
                .map(mapper::toEntity)
                .toList();

        List<MembroEquipeEntity> savedEntities = membroEquipeRepository.saveAll(entities);
        return savedEntities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<MembroEquipeDomain> buscarMembrosPorEquipe(Long equipeId) {
        List<MembroEquipeEntity> entities = membroEquipeRepository.findByEquipeId(equipeId);

        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public int contarTitularesPorEquipe(Long equipeId) {
        return (int) membroEquipeRepository.findByEquipeId(equipeId).stream()
                .filter(m -> m.getTipo() == TipoMembro.TITULAR)
                .count();
    }

    @Override
    public int contarReservasPorEquipe(Long equipeId) {
        return (int) membroEquipeRepository.findByEquipeId(equipeId).stream()
                .filter(m -> m.getTipo() == TipoMembro.RESERVA)
                .count();
    }

    @Override
    public Optional<MembroEquipeDomain> buscarPorId(Long id) {
        return membroEquipeRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void deletarMembroEquipe(Long id) {
        membroEquipeRepository.deleteById(id);
    }

    @Override
    public MembroEquipeDomain atualizarMembros(Long id, MembroEquipeDomain membros) {

        MembroEquipeEntity membroExistente = membroEquipeRepository.findById(id).get();

        if (membros.nickname() != null) {
            membroExistente.setNickname(membros.nickname());
        }
        if (membros.tipo() != null) {
            membroExistente.setTipo(membros.tipo());
        }
        if (membros.rank() != null) {
            membroExistente.setRank(membros.rank());
        }

        MembroEquipeEntity membroAtualizado = membroEquipeRepository.save(membroExistente);
        return mapper.toDomain(membroAtualizado);
    }
}
