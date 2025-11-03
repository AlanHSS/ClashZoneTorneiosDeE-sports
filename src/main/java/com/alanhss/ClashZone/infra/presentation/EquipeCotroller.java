package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.usecases.equipe.CriarEquipeUsecase;
import com.alanhss.ClashZone.core.usecases.usuario.CriarUsuarioUsecase;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("equipe/")
@RequiredArgsConstructor
public class EquipeCotroller {

    private final CriarEquipeUsecase criarEquipeUsecase;
    private final EquipeAtualizarMapper atualizarMapper;
    private final EquipeDtoMapper mapper;

    @PostMapping("criarequipe")
    public ResponseEntity<Map<String, Object>> criarEquipe(@RequestBody EquipeDto equipeDto){

        EquipeDto dtoValidado = mapper.validarEPreparar(equipeDto);

        EquipeDomain novaEquipeDomain = criarEquipeUsecase.execute(mapper.toDomain(dtoValidado));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Equipe criada com sucesso!");
        response.put("Dados da equipe: ", mapper.toDto(novaEquipeDomain));

        return ResponseEntity.ok(response);
    }
}
