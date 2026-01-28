package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.TipoMembro;
import com.alanhss.ClashZone.core.usecases.equipe.*;
import com.alanhss.ClashZone.core.usecases.membro.ListarMembrosPorEquipeUsecase;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.AtualizarEquipeDto;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeDtoMapper;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("clashzone/equipes/")
@RequiredArgsConstructor
public class EquipeCotroller {

    private final CriarEquipeUsecase criarEquipeUsecase;
    private final ListarEquipesUsecase listarEquipesUsecase;
    private final AtualizarEquipeUsecase atualizarEquipeUsecase;
    private final BuscarEquipePorIdUsecase buscarEquipePorIdUsecase;
    private final DeletarEquipePorIdUsecase deletarEquipePorIdUsecase;
    private final ListarEquipesPorLiderUsecase listarEquipesPorLiderUsecase;
    private final ListarMembrosPorEquipeUsecase listarMembrosPorEquipeUsecase;
    private final EquipeAtualizarMapper atualizarMapper;
    private final MembroEquipeDtoMapper membroMapper;
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
    public ResponseEntity<Map<String, Object>> buscarEquipePorId(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role usuarioRole = getUsuarioAutenticado().getRole();

        EquipeDomain equipe = buscarEquipePorIdUsecase.execute(id, usuarioAutenticadoId, usuarioRole);
        List<MembroEquipeDomain> membros = listarMembrosPorEquipeUsecase.execute(equipe.id());

        Map<String, Object> equipeComMembros = new LinkedHashMap<>();
        equipeComMembros.put("equipe", mapper.toDto(equipe));
        equipeComMembros.put("membros da equipe " + equipe.nomeDaEquipe(), membros.stream()
                .map(membroMapper::toDto)
                .sorted(Comparator.comparing(m -> m.tipo() == TipoMembro.RESERVA))
                .toList());
        if (membros.isEmpty()){
            equipeComMembros.remove("membros da equipe " + equipe.nomeDaEquipe());
            equipeComMembros.put("membros", "A equipe " + equipe.nomeDaEquipe() + " não possui membros cadastrados");
        }

        response.put("Suas equipes", equipeComMembros);

        return ResponseEntity.ok(response);
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

    @GetMapping("minhasequipes")
    public ResponseEntity<Map<String, Object>> listarMinhasEquipes(){
        Map<String, Object> response = new HashMap<>();

        Long liderId = getUsuarioAutenticado().getId();
        List<EquipeDomain> listaEquipes = listarEquipesPorLiderUsecase.execute(liderId);

        if(listaEquipes.isEmpty()){
            response.put("Mensagem: ", "Você ainda não criou nenhuma equipe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Map<String, Object>> equipesComMembros = listaEquipes.stream()
                .map(equipe -> {
                    List<MembroEquipeDomain> membros = listarMembrosPorEquipeUsecase.execute(equipe.id());

                    Map<String, Object> equipeComMembros = new LinkedHashMap<>();
                    equipeComMembros.put("equipe", mapper.toDto(equipe));
                    equipeComMembros.put("membros da equipe " + equipe.nomeDaEquipe(), membros.stream()
                            .map(membroMapper::toDto)
                            .sorted(Comparator.comparing(m -> m.tipo() == TipoMembro.RESERVA))
                            .toList());
                    if (membros.isEmpty()){
                        equipeComMembros.remove("membros da equipe " + equipe.nomeDaEquipe());
                        equipeComMembros.put("membros", "A equipe " + equipe.nomeDaEquipe() + " não possui membros cadastrados");
                    }

                    return equipeComMembros;
                })
                .toList();

        response.put("Total encontrado", listaEquipes.size());
        response.put("Suas equipes", equipesComMembros);

        return ResponseEntity.ok(response);

    }
}
