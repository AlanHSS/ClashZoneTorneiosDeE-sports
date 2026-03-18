package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.usecases.torneio.*;
import com.alanhss.ClashZone.infra.dtos.AtualizarTorneioDto;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioDtoMapper;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioEntityMapper;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioFiltroMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioSpecification;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("clashzone/torneios/")
@RequiredArgsConstructor
public class TorneioController {

    private final CriarTorneioUsecase criarTorneioUsecase;
    private final ListarTorneiosUsecase listarTorneiosUsecase;
    private final FiltrosTorneioUsecase filtrosTorneioUsecase;
    private final AtualizarTorneioUsecase atualizarTorneioUsecase;
    private final BuscarTorneioPorIdUsecase buscarTorneioPorIdUsecase;
    private final ListarTorneiosPorCriador listarTorneiosPorCriador;
    private final TorneioRepository torneioRepository;
    private final TorneioAtualizarMapper atualizarMapper;
    private final TorneioFiltroMapper filtroMapper;
    private final TorneioDtoMapper mapper;
    private final TorneioEntityMapper entityMapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("criartorneio")
    public ResponseEntity<Map<String, Object>> criarTorneio(@Valid @RequestBody TorneioDto torneioDto) {

        Long criadorId = getUsuarioAutenticado().getId();

        TorneioDto dtoValidado = mapper.validarEPreparar(torneioDto);

        TorneioDomain torneioDomain = mapper.toDomain(dtoValidado);

        TorneioDomain torneioDomainComCriador = new TorneioDomain(
                torneioDomain.id(),
                torneioDomain.nomeDoTorneio(),
                torneioDomain.descricaoDoTorneio(),
                torneioDomain.inicioDoTorneio(),
                torneioDomain.jogoDoTorneio(),
                torneioDomain.quantidadeDeEquipes(),
                criadorId,
                torneioDomain.statusDoTorneio(),
                torneioDomain.plataforma(),
                torneioDomain.dataCriacao()
        );

        TorneioDomain torneioSalvo = criarTorneioUsecase.execute(torneioDomainComCriador);

        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Torneio criado com sucesso!");
        response.put("Dados do torneio: ", mapper.toDto(torneioDomainComCriador));

        return ResponseEntity.ok(response);
    }

    @GetMapping("listartorneios")
    public List<TorneioDto> listarTorneios() {
        List<TorneioDomain> lista = listarTorneiosUsecase.execute();
        List<TorneioDto> listaConvertida = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            TorneioDomain domain = lista.get(i);
            TorneioDto dto = mapper.toDto(domain);

            listaConvertida.add(dto);
        }
        return listaConvertida;
    }

    @GetMapping("listartorneios/paginado")
    public ResponseEntity<Map<String, Object>> listarTorneiosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataCriacao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<TorneioDto> dtoPage = torneioRepository.findAll(pageable)
                .map(entityMapper::toDomain)
                .map(mapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    @PostMapping("torneiosfiltrados")
    public ResponseEntity<Map<String, Object>> listarTorneiosFiltrados2(@RequestBody FiltroTorneioDto filtroTorneioDto) {
        Map<String, Object> response = new HashMap<>();

        FiltroTorneioDto filtroValidado = filtroMapper.validarEPrepararFiltro(filtroTorneioDto);
        TorneioDomain filtroDomain = filtroMapper.toDomain(filtroValidado);
        List<TorneioDomain> torneiosFiltrados = filtrosTorneioUsecase.execute(filtroDomain);

        if (torneiosFiltrados.isEmpty()) {
            response.put("Mensagem: ", "Não foi encontrado nenhum torneio com essas características");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("Total encontrado: ", torneiosFiltrados.size());
            response.put("Lista de torneios:", torneiosFiltrados.stream()
                    .map(mapper::toDto)
                    .toList());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("torneiosfiltrados/paginado")
    public ResponseEntity<Map<String, Object>> listarTorneiosFiltradosPaginado(
            @RequestBody FiltroTorneioDto filtroTorneioDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        FiltroTorneioDto filtroValidado = filtroMapper.validarEPrepararFiltro(filtroTorneioDto);
        TorneioDomain filtroDomain = filtroMapper.toDomain(filtroValidado);

        Specification<TorneioEntity> spec = Specification.allOf(
                TorneioSpecification.comNome(filtroDomain.nomeDoTorneio()),
                TorneioSpecification.comJogo(filtroDomain.jogoDoTorneio()),
                TorneioSpecification.comPlataforma(filtroDomain.plataforma()),
                TorneioSpecification.comStatus(filtroDomain.statusDoTorneio()),
                TorneioSpecification.comDataInicio(filtroDomain.inicioDoTorneio())
        );

        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataCriacao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<TorneioDto> dtoPage = torneioRepository.findAll(spec, pageable)
                .map(entityMapper::toDomain)
                .map(mapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("atualizartorneio/{id}")
    public ResponseEntity<Map<String, Object>> atualizarTorneio(@PathVariable Long id, @RequestBody AtualizarTorneioDto atualizarTorneioDto) {
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        AtualizarTorneioDto dtoValidado = atualizarMapper.validarEPrepararAtualizacao(atualizarTorneioDto);

        TorneioDomain torneioDomain = atualizarMapper.toDomain(id, dtoValidado);
        TorneioDomain torneioAtualizado = atualizarTorneioUsecase.execute(id, torneioDomain, usuarioAutenticadoId, roleUsuario);
        response.put("Mensagem: ", "Torneio atualizado com sucesso!");
        response.put("Dados do torneio: ", mapper.toDto(torneioAtualizado));

        return ResponseEntity.ok(response);
    }

    @GetMapping("paginadotorneio/{id}")
    public TorneioDto buscarTorneioPorId(@PathVariable Long id) {
        TorneioDomain torneioDomain = buscarTorneioPorIdUsecase.execute(id);
        TorneioDto torneioDto = mapper.toDto(torneioDomain);

        return torneioDto;
    }

    @GetMapping("meustorneios")
    public List<TorneioDto> listarMeusTorneios() {

        Long criadorId = getUsuarioAutenticado().getId();
        List<TorneioDomain> lista = listarTorneiosPorCriador.execute(criadorId);
        return lista.stream()
                .map(mapper::toDto)
                .toList();
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
