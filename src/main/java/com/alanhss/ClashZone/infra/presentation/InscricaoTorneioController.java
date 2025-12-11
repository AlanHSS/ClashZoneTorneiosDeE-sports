package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.usecases.inscricao.CriarInscricaoTorneioUsecase;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoTorneioDto;
import com.alanhss.ClashZone.infra.mappers.InscricaoMappers.InscricaoTorneioDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("clashzone/inscricao/")
@RequiredArgsConstructor
public class InscricaoTorneioController {

    private final CriarInscricaoTorneioUsecase criarInscricaoTorneioUsecase;
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

}
