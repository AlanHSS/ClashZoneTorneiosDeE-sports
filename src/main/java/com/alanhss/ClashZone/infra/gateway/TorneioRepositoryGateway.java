package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.infra.mappers.TorneioEntityMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<TorneioDomain> listaConvertida = new ArrayList<>();

        for(int i = 0; i < lista.size(); i++){
            TorneioEntity entity = lista.get(i);
            TorneioDomain domain = mapper.toDomain(entity);

            listaConvertida.add(domain);
        }
        return listaConvertida;
    }
}
