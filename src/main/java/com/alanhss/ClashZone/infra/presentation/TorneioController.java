package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.usecases.torneio.*;
import com.alanhss.ClashZone.infra.dtos.AtualizarTorneioDto;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;
import com.alanhss.ClashZone.infra.dtos.TorneioDto;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioDtoMapper;
import com.alanhss.ClashZone.infra.mappers.TorneiosMappers.TorneioFiltroMapper;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioRepository;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final BuscarTorneioPorId buscarTorneioPorId;
    private final ListarTorneiosPorCriador listarTorneiosPorCriador;
    private final TorneioRepository torneioRepository;
    private final TorneioAtualizarMapper atualizarMapper;
    private final TorneioFiltroMapper filtroMapper;
    private final TorneioDtoMapper mapper;

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
        TorneioDomain torneioDomain = buscarTorneioPorId.execute(id);
        TorneioDto torneioDto = mapper.toDto(torneioDomain);

        return torneioDto;
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("meustorneios")
    public List<TorneioDto> listarMeusTorneios() {

        Long criadorId = getUsuarioAutenticado().getId();
        List<TorneioDomain> lista = listarTorneiosPorCriador.execute(criadorId);
        return lista.stream()
                .map(mapper::toDto)
                .toList();
    }

}
