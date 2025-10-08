package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecase;
import com.alanhss.ClashZone.core.usecases.FiltrosTorneioUsecase;
import com.alanhss.ClashZone.core.usecases.ListarTorneiosUsecase;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TorneioController {

    private final CriarTorneioUsecase criarTorneioUsecase;
    private final ListarTorneiosUsecase listarTorneiosUsecase;
    private final FiltrosTorneioUsecase filtrosTorneioUsecase;
    private final TorneioDtoMapper mapper;

    @PostMapping("criartorneio")
    public ResponseEntity<Map<String, Object>> criarTorneio(@RequestBody TorneioDto torneioDto){
        TorneioDomain novoTorneioDomain = criarTorneioUsecase.execute(mapper.toDomain(torneioDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Torneio criado com sucesso!");
        response.put("Dados do torneio: ", mapper.toDto(novoTorneioDomain));

        return ResponseEntity.ok(response);
    }

    @GetMapping("listartorneios")
    public List<TorneioDto> listarTorneios(){
        List<TorneioDomain> lista = listarTorneiosUsecase.execute();
        List<TorneioDto> listaConvertida = new ArrayList<>();

        for(int i = 0; i < lista.size(); i++){
            TorneioDomain domain = lista.get(i);
            TorneioDto dto = mapper.toDto(domain);

            listaConvertida.add(dto);
        }
        return listaConvertida;
    }

    @PostMapping("torneiosfiltrados")
    public List<TorneioDto> listarTorneiosFiltrados(@RequestBody FiltroTorneioDto filtroTorneioDto){
        return filtrosTorneioUsecase.execute(filtroTorneioDto)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

}
