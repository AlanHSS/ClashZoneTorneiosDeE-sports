package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.TipoMembro;
import com.alanhss.ClashZone.core.usecases.membro.AdicionarMembrosEquipeUsecase;
import com.alanhss.ClashZone.core.usecases.membro.ListarMembrosPorEquipeUsecase;
import com.alanhss.ClashZone.infra.dtos.EquipesDtos.EquipeDto;
import com.alanhss.ClashZone.infra.dtos.MembrosDtos.MembroEquipeDto;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.MembrosMappers.MembroEquipeDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("clashzone/equipes/{equipeId}/membros/")
@RequiredArgsConstructor
public class MembroEquipeController {

    private final AdicionarMembrosEquipeUsecase adicionarMembrosEquipeUsecase;
    private final ListarMembrosPorEquipeUsecase listarMembrosPorEquipeUsecase;
    private final MembroEquipeAtualizarMapper atualizarMapper;
    private final MembroEquipeDtoMapper mapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PostMapping("adicionar")
    public ResponseEntity<Map<String, Object>> adicionarMembro(@PathVariable Long equipeId, @Valid @RequestBody List<MembroEquipeDto> membroEquipeDto){
        Map<String, Object> response = new HashMap<>();

        UsuariosEntity usuarioAutenticado = getUsuarioAutenticado();

        List<MembroEquipeDto> membrosValidados = membroEquipeDto.stream()
                .map(mapper::validarEPreparar)
                .toList();

        List<MembroEquipeDomain> membrosDomain = membrosValidados.stream()
                .map(dto -> mapper.toDomainWithEquipeId(equipeId, dto))
                .toList();

        List<MembroEquipeDomain> membrosAdicionados = adicionarMembrosEquipeUsecase.execute(
                equipeId,
                membrosDomain,
                usuarioAutenticado.getId(),
                usuarioAutenticado.getRole()
        );

        response.put("Mensagem", "Membros adicionados com sucesso!");
        response.put("Total adicionado", membrosAdicionados.size());
        response.put("Membros", membrosAdicionados.stream()
                .map(mapper::toDto)
                .toList());

        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listarmembros")
    public List<MembroEquipeDto> listarMembrosPorEquipe(@PathVariable Long equipeId){
        Long equipe = getUsuarioAutenticado().getId();
        List<MembroEquipeDomain> lista = listarMembrosPorEquipeUsecase.execute(equipeId);

        return lista.stream()
                .map(mapper::toDto)
                .sorted(Comparator.comparing(m -> m.tipo() == TipoMembro.RESERVA))
                .toList();
    }
}
