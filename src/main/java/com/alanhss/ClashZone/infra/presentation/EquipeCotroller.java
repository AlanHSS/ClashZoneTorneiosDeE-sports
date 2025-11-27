package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.usecases.equipe.*;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.AtualizarEquipeDto;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final AtualizarEquipeUsecase atualizarEquipeUsecase;
    private final BuscarEquipePorIdUsecase buscarEquipePorIdUsecase;
    private final DeletarEquipePorIdUsecase deletarEquipePorIdUsecase;
    private final EquipeAtualizarMapper atualizarMapper;
    private final EquipeDtoMapper mapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listartodasequipes")
    public List<EquipeDto> listarTodasEquipes(){
        List<EquipeDomain> lista = listarEquipesUsecase.execute();

        return lista.stream().map(mapper::toDto)
                .toList();

    }

    @PatchMapping("atualizarequipe/{id}")
    public ResponseEntity<Map<String, Object>> atualizarEquipe(@PathVariable Long id, @RequestBody AtualizarEquipeDto atualizarEquipeDto){
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        AtualizarEquipeDto dtoValidado = atualizarMapper.validarEPreparar(atualizarEquipeDto);

        EquipeDomain equipeDomain = atualizarMapper.toDomain(id, dtoValidado);
        EquipeDomain equipeAtualizada = atualizarEquipeUsecase.execute(id, equipeDomain, usuarioAutenticadoId, roleUsuario);

        response.put("Mensagem", "Equipe atualizada com sucesso!");
        response.put("Dados da equipe", mapper.toDto(equipeAtualizada));

        return ResponseEntity.ok(response);
    }

    @GetMapping("informacoesdaequipe/{id}")
    public EquipeDto buscarEquipePorId(@PathVariable Long id){
        return mapper.toDto(buscarEquipePorIdUsecase.execute(id));
    }

    @DeleteMapping("deletarequipe/{id}")
    public ResponseEntity<Map<String, Object>> deletarEquipe(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        deletarEquipePorIdUsecase.execute(id, usuarioAutenticadoId, roleUsuario);

        response.put("Mensagem", "Equipe deletada com sucesso!");
        response.put("Id deletado", id);

        return ResponseEntity.ok(response);
    }
}
