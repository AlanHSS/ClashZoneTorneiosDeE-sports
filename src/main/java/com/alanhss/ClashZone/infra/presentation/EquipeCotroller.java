package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.usecases.equipe.CriarEquipeUsecase;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesUsecase;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("clashzone/equipes/")
@RequiredArgsConstructor
public class EquipeCotroller {

    private final CriarEquipeUsecase criarEquipeUsecase;
    private final ListarEquipesUsecase listarEquipesUsecase;
    private final EquipeAtualizarMapper atualizarMapper;
    private final EquipeDtoMapper mapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PostMapping("criarequipe")
    public ResponseEntity<Map<String, Object>> criarEquipe(@Valid @RequestBody EquipeDto equipeDto){

        Long criadorId = getUsuarioAutenticado().getId();

        EquipeDto dtoValidado = mapper.validarEPreparar(equipeDto);

        EquipeDomain equipeDomain = mapper.toDomain(equipeDto);

        EquipeDomain equipeDomainComCriador = new EquipeDomain(
                equipeDomain.id(),
                equipeDomain.nomeDaEquipe(),
                criadorId,
                equipeDomain.jogo(),
                equipeDomain.dataCriacao(),
                equipeDomain.inscrita()
        );

        EquipeDomain novaEquipeDomain = criarEquipeUsecase.execute(equipeDomainComCriador);

        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Equipe criada com sucesso!");
        response.put("Dados da equipe: ", mapper.toDto(equipeDomainComCriador));

        return ResponseEntity.ok(response);
    }

    @GetMapping("listarequipes")
    public List<EquipeDto> listarEquipes(){
        List<EquipeDomain> lista = listarEquipesUsecase.execute();

        return lista.stream().map(mapper::toDto)
                .toList();

    }
}
