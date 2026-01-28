package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeEntityMapper;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<EquipeDomain> buscarPorId(Long id) {
        return equipeRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public EquipeDomain atualizarEquipe(Long id, EquipeDomain equipeDomain) {

        EquipeEntity equipeExistente = equipeRepository.findById(id).get();

        if (equipeDomain.nomeDaEquipe() != null){
            equipeExistente.setNomeDaEquipe(equipeDomain.nomeDaEquipe());
        }
        if (equipeDomain.jogo() != null){
            equipeExistente.setJogo(equipeDomain.jogo());
        }
        equipeExistente.setInscrita(equipeDomain.inscrita());

        EquipeEntity equipeAtualizada = equipeRepository.save(equipeExistente);

        return mapper.toDomain(equipeAtualizada);
    }

    @Override
    public void deletarEquipe(Long id) {
        equipeRepository.deleteById(id);
    }

    @Override
    public List<EquipeDomain> listarEquipesPorLider(Long id) {
        List<EquipeEntity> lista = equipeRepository.findByLiderId(id);
        return lista.stream()
                .map(mapper::toDomain)
                .toList();
    }
}
