package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecase;
import com.alanhss.ClashZone.core.usecases.ListarTorneiosUsecase;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneioDtoMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TorneioController {

    private final CriarTorneioUsecase criarTorneioUsecase;
    private final ListarTorneiosUsecase listarTorneiosUsecase;
    private final TorneioDtoMapper torneioDtoMapper;

    @PostMapping("criartorneio")
    public ResponseEntity<Map<String, Object>> criarTorneio(@RequestBody TorneioDto torneioDto){
        TorneioDomain novoTorneioDomain = criarTorneioUsecase.execute(torneioDtoMapper.toDomain(torneioDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Torneio criado com sucesso!");
        response.put("Dados do torneio: ", torneioDtoMapper.toDto(novoTorneioDomain));

        return ResponseEntity.ok(response);
    }

    @GetMapping("listartorneios")
    public List<TorneioDto> listarTorneios(){
        List<TorneioDomain> lista = listarTorneiosUsecase.execute();
        List<TorneioDto> listaConvertida = new ArrayList<>();

        for(int i = 0; i < lista.size(); i++){
            TorneioDomain domain = lista.get(i);
            TorneioDto dto = torneioDtoMapper.toDto(domain);

            listaConvertida.add(dto);
        }
        return listaConvertida;
    }

}
