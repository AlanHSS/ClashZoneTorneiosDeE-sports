package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesPorLiderUsecase;
import com.alanhss.ClashZone.core.usecases.inscricao.*;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.AtualizarInscricaoDto;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoDetalhadaDto;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoTorneioDto;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoDetalhadaDtoMapper;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioDtoMapper;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioEntity;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioRepository;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final AtualizarInscricaoUsecase atualizarInscricaoUsecase;
    private final BuscarInscricaoPorIdUsecase buscarInscricaoPorIdUsecase;
    private final InscricaoDetalhadaDtoMapper detalhadaMapper;
    private final InscricaoTorneioAtualizarMapper atualizarMapper;
    private final InscricaoTorneioDtoMapper mapper;

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
}
