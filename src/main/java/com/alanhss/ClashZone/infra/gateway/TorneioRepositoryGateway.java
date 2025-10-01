package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.infra.mappers.TorneioEntityMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TorneioRepositoryGateway implements TorneioGateway {

    private final TorneioRepository torneioRepository;
    private final TorneioEntityMapper mapper;

    @Override
    public TorneioDomain criarTorneio(TorneioDomain torneioDomain) {
        TorneioEntity torneioEntity = mapper.toEntity(torneioDomain);
        TorneioEntity novoTorneio = torneioRepository.save(torneioEntity);
        return mapper.toDomain(novoTorneio);
    }

    @Override
    public List<TorneioDomain> listarTorneios() {
        List<TorneioEntity> lista = torneioRepository.findAll();
        return lista.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
