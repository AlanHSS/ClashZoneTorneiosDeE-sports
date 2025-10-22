package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioEntityMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.TorneioSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public List<TorneioDomain> filtrarTorneios(FiltroTorneioDto filtroTorneioDto) {
        Specification<TorneioEntity> spec = Specification.allOf(
                TorneioSpecification.comNome(filtroTorneioDto.nomeDoTorneio()),
                TorneioSpecification.comJogo(filtroTorneioDto.jogoDoTorneio()),
                TorneioSpecification.comPlataforma(filtroTorneioDto.plataforma()),
                TorneioSpecification.comStatus(filtroTorneioDto.statusDoTorneio()),
                TorneioSpecification.comDataInicio(filtroTorneioDto.inicioDoTorneio())
        );

        return torneioRepository.findAll(spec)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public TorneioDomain atualizarTorneio(Long id, TorneioDomain torneioDomain) {
        TorneioEntity torneioExistente = torneioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneio não encontrado com id: " + id));

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
