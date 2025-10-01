package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecase;
import com.alanhss.ClashZone.core.usecases.ListarTorneiosUsecase;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TorneioController {

    private final CriarTorneioUsecase criarTorneioUsecase;
    private final ListarTorneiosUsecase listarTorneiosUsecase;
    private final TorneioDtoMapper torneioDtoMapper;

    @PostMapping("criartorneio")
    public TorneioDto criarTorneio(@RequestBody TorneioDto torneioDto){
        TorneioDomain novoTorneioDomain = criarTorneioUsecase.execute(torneioDtoMapper.toDomain(torneioDto));
        return torneioDtoMapper.toDto(novoTorneioDomain);
    }

    @GetMapping("listartorneios")
    public List<TorneioDto> listarTorneios(){
        return listarTorneiosUsecase.execute().stream()
                .map(torneioDtoMapper::toDto)
                .collect(Collectors.toList());
    }

}
