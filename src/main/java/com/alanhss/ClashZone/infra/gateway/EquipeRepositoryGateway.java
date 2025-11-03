package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeEntityMapper;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EquipeRepositoryGateway implements EquipeGateway {

    private final EquipeRepository equipeRepository;
    private final EquipeEntityMapper mapper;

    @Override
    public EquipeDomain criarEquipe(EquipeDomain equipeDomain) {
        EquipeEntity equipeEntity = mapper.toEntity(equipeDomain);
        EquipeEntity novaEquipe = equipeRepository.save(equipeEntity);

        return mapper.toDomain(novaEquipe);
    }

    @Override
    public List<EquipeDomain> listarEquipes() {
        List<EquipeEntity> lista = equipeRepository.findAll();

        return lista.stream()
                .map(mapper::toDomain)
                .toList();

    }
}
