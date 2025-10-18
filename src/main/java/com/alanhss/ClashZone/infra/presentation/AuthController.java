package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.usecases.usuario.CriarUsuarioUsecase;
import com.alanhss.ClashZone.infra.dtos.UsuariosDto;
import com.alanhss.ClashZone.infra.mappers.UsuariosDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("auth/")
@RequiredArgsConstructor
public class AuthController {

    private final CriarUsuarioUsecase criarUsuarioUsecase;
    private final UsuariosDtoMapper mapper;

    @PostMapping("criarusuario")
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody UsuariosDto usuariosDto){
        UsuariosDomain novoUsuarioDomain = criarUsuarioUsecase.execute(mapper.toDomain(usuariosDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Usuário criado com sucesso!");
        response.put("Dados do usuário: ", mapper.toDto(novoUsuarioDomain));

        return ResponseEntity.ok(response);
    }
}
