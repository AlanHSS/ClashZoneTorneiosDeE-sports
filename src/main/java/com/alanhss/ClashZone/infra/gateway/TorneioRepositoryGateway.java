package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioEntityMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<TorneioDomain> filtrarTorneios(TorneioDomain torneioDomain) {
        Specification<TorneioEntity> spec = Specification.allOf(
                TorneioSpecification.comNome(torneioDomain.nomeDoTorneio()),
                TorneioSpecification.comJogo(torneioDomain.jogoDoTorneio()),
                TorneioSpecification.comPlataforma(torneioDomain.plataforma()),
                TorneioSpecification.comStatus(torneioDomain.statusDoTorneio()),
                TorneioSpecification.comDataInicio(torneioDomain.inicioDoTorneio())
        );

        return torneioRepository.findAll(spec)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<TorneioDomain> buscarPorId(Long id) {
        return torneioRepository.findById(id)
                .map(mapper::toDomain);
    }

    
    @Override
    public TorneioDomain atualizarTorneio(Long id, TorneioDomain torneioDomain) {
        TorneioEntity torneioExistente = torneioRepository.findById(id).get();

        if (torneioDomain.nomeDoTorneio() != null) {
            torneioExistente.setNomeDoTorneio(torneioDomain.nomeDoTorneio());
        }
        if (torneioDomain.descricaoDoTorneio() != null) {
            torneioExistente.setDescricaoDoTorneio(torneioDomain.descricaoDoTorneio());
        }
        if (torneioDomain.inicioDoTorneio() != null) {
            torneioExistente.setInicioDoTorneio(torneioDomain.inicioDoTorneio());
        }
        if (torneioDomain.jogoDoTorneio() != null) {
            torneioExistente.setJogoDoTorneio(torneioDomain.jogoDoTorneio());
        }
        if (torneioDomain.quantidadeDeEquipes() != null) {
            torneioExistente.setQuantidadeDeEquipes(torneioDomain.quantidadeDeEquipes());
        }
        if (torneioDomain.statusDoTorneio() != null) {
            torneioExistente.setStatusDoTorneio(torneioDomain.statusDoTorneio());
        }
        if (torneioDomain.plataforma() != null) {
            torneioExistente.setPlataforma(torneioDomain.plataforma());
        }

        TorneioEntity torneioAtualizado = torneioRepository.save(torneioExistente);
        return mapper.toDomain(torneioAtualizado);
    }
}
