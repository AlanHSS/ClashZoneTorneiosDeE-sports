package com.alanhss.ClashZone.infra.mappers.InscricaoMappers;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeRepository;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InscricaoTorneioEntityMapper {

    private final TorneioRepository torneioRepository;
    private final EquipeRepository equipeRepository;

    public InscricaoTorneioEntity toEntity(InscricaoTorneioDomain domain) {
        InscricaoTorneioEntity entity = new InscricaoTorneioEntity();
        entity.setId(domain.id());
        entity.setStatusInscricao(domain.statusInscricao());
        entity.setMotivoRecusa(domain.motivoRecusa());
        entity.setDataInscricao(domain.dataInscricao());

        if (domain.torneioId() != null) {
            TorneioEntity torneio = torneioRepository.findById(domain.torneioId())
                    .orElseThrow(() -> new RuntimeException("Torneio não encontrado com ID: " + domain.torneioId()));
            entity.setTorneioId(torneio);
        }

        if (domain.equipeId() != null) {
            EquipeEntity equipe = equipeRepository.findById(domain.equipeId())
                    .orElseThrow(() -> new RuntimeException("Equipe não encontrada com ID: " + domain.equipeId()));
            entity.setEquipeId(equipe);
        }

        return entity;
    }

    public InscricaoTorneioDomain toDomain(InscricaoTorneioEntity entity) {
        return new InscricaoTorneioDomain(
                entity.getId(),
                entity.getTorneioId() != null ? entity.getTorneioId().getId() : null,
                entity.getEquipeId() != null ? entity.getEquipeId().getId() : null,
                entity.getStatusInscricao(),
                entity.getMotivoRecusa(),
                entity.getDataInscricao()

        );
    }

}
