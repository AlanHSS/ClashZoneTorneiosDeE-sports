package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesPorLiderUsecase;
import com.alanhss.ClashZone.core.usecases.inscricao.*;
import com.alanhss.ClashZone.core.usecases.membro.ListarMembrosPorEquipeUsecase;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.AtualizarInscricaoDto;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoDetalhadaDto;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoTorneioDto;
import com.alanhss.ClashZone.infra.dtos.MembrosDtos.MembroEquipeDto;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoDetalhadaDtoMapper;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioDtoMapper;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioEntity;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioRepository;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeRepository;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("clashzone/inscricao/")
@RequiredArgsConstructor
public class InscricaoTorneioController {

    private final CriarInscricaoTorneioUsecase criarInscricaoTorneioUsecase;
    private final ListarInscricoesPorTorneioUsecase listarInscricoesPorTorneioUsecase;
    private final ListarInscricoesPorEquipeUsecase listarInscricoesPorEquipeUsecase;
    private final ListarEquipesPorLiderUsecase listarEquipesPorLiderUsecase;
    private final InscricaoTorneioRepository inscricaoTorneioRepository;
    private final TorneioRepository torneioRepository;
    private final EquipeRepository equipeRepository;
    private final AtualizarInscricaoUsecase atualizarInscricaoUsecase;
    private final BuscarInscricaoPorIdUsecase buscarInscricaoPorIdUsecase;
    private final ListarMembrosPorEquipeUsecase listarMembrosPorEquipeUsecase;
    private final InscricaoDetalhadaDtoMapper detalhadaMapper;
    private final InscricaoTorneioAtualizarMapper atualizarMapper;
    private final InscricaoTorneioDtoMapper mapper;
    private final MembroEquipeDtoMapper membroEquipeDtoMapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PostMapping("criar")
    public ResponseEntity<Map<String, Object>> criarInscricao(@Valid @RequestBody InscricaoTorneioDto inscricaoDto) {
        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();

        InscricaoTorneioDomain inscricaoDomain = mapper.toDomain(inscricaoDto);

        InscricaoTorneioDomain novaInscricao = criarInscricaoTorneioUsecase.execute(inscricaoDomain, usuarioAutenticadoId);

        response.put("Mensagem", "Inscrição criada com sucesso! Aguardando aprovação.");
        response.put("Inscrição", mapper.toDto(novaInscricao));

        return ResponseEntity.ok(response);
    }

    @GetMapping("torneio/{torneioId}")
    public ResponseEntity<Map<String, Object>> listarInscricoesPorTorneio(@PathVariable Long torneioId, @RequestParam(required = false) StatusInscricao status) {

        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        List<InscricaoTorneioDomain> inscricoes = listarInscricoesPorTorneioUsecase.execute(
                torneioId,
                status,
                usuarioAutenticadoId,
                roleUsuario
        );

        if (inscricoes.isEmpty()) {
            String mensagem = status != null
                    ? "Não há inscrições com status " + status.name() + " neste torneio"
                    : "Não há inscrições neste torneio";
            response.put("Mensagem", mensagem);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<InscricaoDetalhadaDto> inscricoesDetalhadas = inscricoes.stream()
                .map(inscricao -> {
                    InscricaoTorneioEntity entity = inscricaoTorneioRepository.findById(inscricao.id()).get();
                    return detalhadaMapper.toDto(entity);
                })
                .sorted(Comparator.comparing(InscricaoDetalhadaDto::dataInscricao).reversed())
                .toList();

        response.put("Torneio ID", torneioId);
        if (status != null) {
            response.put("Filtro Status", status.getDescricao());
        }
        response.put("Total encontrado", inscricoesDetalhadas.size());
        response.put("Inscrições", inscricoesDetalhadas);

        return ResponseEntity.ok(response);
    }

    @GetMapping("equipe/{equipeId}")
    public ResponseEntity<Map<String, Object>> listarInscricoesPorEquipe(
            @PathVariable Long equipeId,
            @RequestParam(required = false) StatusInscricao status) {

        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        List<InscricaoTorneioDomain> inscricoes = listarInscricoesPorEquipeUsecase.execute(
                equipeId,
                status,
                usuarioAutenticadoId,
                roleUsuario
        );

        if (inscricoes.isEmpty()) {
            String mensagem = status != null
                    ? "Esta equipe não possui inscrições com status " + status.name()
                    : "Esta equipe ainda não se inscreveu em nenhum torneio";
            response.put("Mensagem", mensagem);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Busca dados detalhados de cada inscrição
        List<InscricaoDetalhadaDto> inscricoesDetalhadas = inscricoes.stream()
                .map(inscricao -> {
                    InscricaoTorneioEntity entity = inscricaoTorneioRepository.findById(inscricao.id()).get();
                    return detalhadaMapper.toDto(entity);
                })
                .sorted(Comparator.comparing(InscricaoDetalhadaDto::dataInscricao).reversed())
                .toList();

        response.put("Equipe ID", equipeId);
        if (status != null) {
            response.put("Filtro Status", status.getDescricao());
        }
        response.put("Total encontrado", inscricoesDetalhadas.size());
        response.put("Histórico de Inscrições", inscricoesDetalhadas);

        return ResponseEntity.ok(response);
    }

    @GetMapping("minhasinscricoes")
    public ResponseEntity<Map<String, Object>> listarMinhasInscricoes(
            @RequestParam(required = false) StatusInscricao status) {

        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role usuarioAutenticadoRole = getUsuarioAutenticado().getRole();

        List<EquipeDomain> minhasEquipes = listarEquipesPorLiderUsecase.execute(usuarioAutenticadoId);

        if (minhasEquipes.isEmpty()) {
            response.put("Mensagem", "Você não possui nenhuma equipe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<InscricaoDetalhadaDto> todasInscricoes = minhasEquipes.stream()
                .flatMap(equipe -> {
                    List<InscricaoTorneioDomain> inscricoes = listarInscricoesPorEquipeUsecase.execute(equipe.id(), status, usuarioAutenticadoId, usuarioAutenticadoRole);

                    if (status != null) {
                        inscricoes = inscricoes.stream()
                                .filter(i -> i.statusInscricao() == status)
                                .toList();
                    }

                    return inscricoes.stream()
                            .map(inscricao -> {
                                InscricaoTorneioEntity entity = inscricaoTorneioRepository.findById(inscricao.id()).get();
                                return detalhadaMapper.toDto(entity);
                            });
                })
                .sorted(Comparator.comparing(InscricaoDetalhadaDto::dataInscricao).reversed())
                .toList();

        if (todasInscricoes.isEmpty()) {
            String mensagem = status != null
                    ? "Suas equipes não possuem inscrições com status " + status.name()
                    : "Suas equipes ainda não se inscreveram em nenhum torneio";
            response.put("Mensagem", mensagem);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (status != null) {
            response.put("Filtro Status", status.getDescricao());
        }
        response.put("Total de equipes", minhasEquipes.size());
        response.put("Total de inscrições", todasInscricoes.size());
        response.put("Minhas Inscrições", todasInscricoes);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("atualizar/{inscricaoId}")
    public ResponseEntity<Map<String, Object>> atualizarInscricao(@PathVariable Long inscricaoId, @Valid @RequestBody AtualizarInscricaoDto atualizarInscricaoDto) {

        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        AtualizarInscricaoDto dtoValidado = atualizarMapper.validarEPreparar(atualizarInscricaoDto);

        InscricaoTorneioDomain inscricaoExistente = buscarInscricaoPorIdUsecase.execute(inscricaoId);

        InscricaoTorneioDomain inscricaoDomain = atualizarMapper.toDomain(
                inscricaoId,
                inscricaoExistente.torneioId(),
                inscricaoExistente.equipeId(),
                dtoValidado
        );

        InscricaoTorneioDomain inscricaoAtualizada = atualizarInscricaoUsecase.execute(
                inscricaoId,
                inscricaoDomain,
                usuarioAutenticadoId,
                roleUsuario
        );

        InscricaoTorneioEntity entity = inscricaoTorneioRepository.findById(inscricaoAtualizada.id()).get();
        InscricaoDetalhadaDto inscricaoDetalhada = detalhadaMapper.toDto(entity);

        response.put("Mensagem", "Inscrição atualizada com sucesso!");
        response.put("Inscrição", inscricaoDetalhada);

        return ResponseEntity.ok(response);
    }

    @GetMapping("torneio/{torneioId}/paginado")
    public ResponseEntity<Map<String, Object>> listarInscricoesPorTorneioPaginado(
            @PathVariable Long torneioId,
            @RequestParam(required = false) StatusInscricao status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        var torneio = torneioRepository.findById(torneioId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(torneioId, "torneio"));

        Long criadorId = torneio.getCriadorId() != null ? torneio.getCriadorId().getId() : null;
        if (roleUsuario != Role.ADMIN && (criadorId == null || !criadorId.equals(usuarioAutenticadoId))) {
            throw new AcessoNegadoException("Apenas o criador do torneio ou um administrador podem visualizar as inscrições");
        }

        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataInscricao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<InscricaoDetalhadaDto> dtoPage = (status != null
                ? inscricaoTorneioRepository.findByTorneioIdIdAndStatusInscricao(torneioId, status, pageable)
                : inscricaoTorneioRepository.findByTorneioIdId(torneioId, pageable))
                .map(detalhadaMapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("equipe/{equipeId}/paginado")
    public ResponseEntity<Map<String, Object>> listarInscricoesPorEquipePaginado(
            @PathVariable Long equipeId,
            @RequestParam(required = false) StatusInscricao status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        var equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipe.getLiderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem visualizar o histórico de inscrições");
        }

        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataInscricao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<InscricaoDetalhadaDto> dtoPage = (status != null
                ? inscricaoTorneioRepository.findByEquipeIdIdAndStatusInscricao(equipeId, status, pageable)
                : inscricaoTorneioRepository.findByEquipeIdId(equipeId, pageable))
                .map(detalhadaMapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("minhasinscricoes/paginado")
    public ResponseEntity<Map<String, Object>> listarMinhasInscricoesPaginado(
            @RequestParam(required = false) StatusInscricao status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") List<String> sort
    ) {
        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();

        List<Long> equipeIds = equipeRepository.findByLiderId(usuarioAutenticadoId).stream()
                .map(e -> e.getId())
                .toList();

        Sort sortObj = parseSort(sort, Sort.by(Sort.Direction.DESC, "dataInscricao"));
        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<InscricaoDetalhadaDto> dtoPage = equipeIds.isEmpty()
                ? Page.<InscricaoTorneioEntity>empty(pageable).map(detalhadaMapper::toDto)
                : inscricaoTorneioRepository.findByEquipeIdsAndStatus(equipeIds, status, pageable).map(detalhadaMapper::toDto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", dtoPage.getNumber());
        response.put("size", dtoPage.getSize());
        response.put("totalElements", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        response.put("content", dtoPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("torneio/{torneioId}/equipe/{equipeId}/membros")
    public ResponseEntity<Map<String, Object>> listarMembrosDaEquipeInscrita(
            @PathVariable Long torneioId,
            @PathVariable Long equipeId) {

        Map<String, Object> response = new LinkedHashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        // Reusa a mesma regra de acesso de "listar inscricoes por torneio".
        List<InscricaoTorneioDomain> inscricoes = listarInscricoesPorTorneioUsecase.execute(
                torneioId,
                null,
                usuarioAutenticadoId,
                roleUsuario
        );

        boolean equipeEstaInscrita = inscricoes.stream().anyMatch(i -> i.equipeId().equals(equipeId));
        if (!equipeEstaInscrita) {
            response.put("Mensagem", "Equipe nao encontrada nas inscricoes deste torneio");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<MembroEquipeDomain> membros = listarMembrosPorEquipeUsecase.execute(equipeId);
        List<MembroEquipeDto> membrosDto = membros.stream()
                .map(membroEquipeDtoMapper::toDto)
                .toList();

        response.put("Equipe ID", equipeId);
        response.put("Total encontrado", membrosDto.size());
        response.put("Membros", membrosDto);

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
