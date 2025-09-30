package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecase;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TorneioController {

    private final CriarTorneioUsecase criarTorneioUsecase;
    private final TorneioDtoMapper torneioDtoMapper;

    @PostMapping("criartorneio")
    public TorneioDto criartorneio(@RequestBody TorneioDto torneioDto){
        TorneioDomain novoTorneioDomain = criarTorneioUsecase.execute(torneioDtoMapper.toDomain(torneioDto));
        return torneioDtoMapper.toDto(novoTorneioDomain);
    }

    @GetMapping
    public String listarTorneios(){
        return "Lista de torneios: ";
    }

}
