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
import com.alanhss.ClashZone.infra.mappers.EquipeMappers.EquipeEntityMapper;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeRepository;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final EquipeRepository equipeRepository;
    private final EquipeEntityMapper equipeEntityMapper;

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
        response.put("Dados da equipe: ", mapper.toDto(novaEquipeDomain));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listartodasequipes")
    public List<EquipeDto> listarTodasEquipes(){
        List<EquipeDomain> lista = listarEquipesUsecase.execute();

        return lista.stream().map(mapper::toDto)
                .toList();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listartodasequipes/paginado")
    public ResponseEntity<Map<String, Object>> listarTodasEquipesPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataCriacao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<EquipeDto> dtoPage = equipeRepository.findAll(pageable)
                .map(equipeEntityMapper::toDomain)
                .map(mapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
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

    @GetMapping("minhasequipes/paginado")
    public ResponseEntity<Map<String, Object>> listarMinhasEquipesPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Long liderId = getUsuarioAutenticado().getId();

        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataCriacao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<EquipeDto> dtoPage = equipeRepository.findByLiderId(liderId, pageable)
                .map(equipeEntityMapper::toDomain)
                .map(mapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    private Sort parseSort(List<String> sortParams, Sort defaultSort) {
        if (sortParams == null || sortParams.isEmpty()) return defaultSort;

        Sort result = Sort.unsorted();
        for (String raw : sortParams) {
            if (raw == null || raw.isBlank()) continue;

            String[] parts = raw.split(",");
            String property = parts[0].trim();
            if (property.isEmpty()) continue;

            Sort.Direction direction = Sort.Direction.ASC;
            if (parts.length > 1) {
                try {
                    direction = Sort.Direction.fromString(parts[1].trim());
                } catch (IllegalArgumentException ignored) {
                    direction = Sort.Direction.ASC;
                }
            }

            result = result.and(Sort.by(direction, property));
        }

        return result.isUnsorted() ? defaultSort : result;
    }
}
