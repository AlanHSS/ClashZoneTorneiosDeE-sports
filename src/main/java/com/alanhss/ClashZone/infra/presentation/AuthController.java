package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.usecases.usuario.CriarUsuarioUsecase;
import com.alanhss.ClashZone.core.usecases.usuario.ListarUsuariosUsecase;
import com.alanhss.ClashZone.infra.dtos.UsuariosDto;
import com.alanhss.ClashZone.infra.mappers.UsuariosDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("auth/")
@RequiredArgsConstructor
public class AuthController {

    private final CriarUsuarioUsecase criarUsuarioUsecase;
    private final ListarUsuariosUsecase listarUsuariosUsecase;
    private final UsuariosDtoMapper mapper;

    @PostMapping("criarusuario")
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody UsuariosDto usuariosDto){
        UsuariosDomain novoUsuarioDomain = criarUsuarioUsecase.execute(mapper.toDomain(usuariosDto));
        Map<String, Object> response = new HashMap<>();
        response.put("Mensagem: ", "Usuário criado com sucesso!");
        response.put("Dados do usuário: ", mapper.toDto(novoUsuarioDomain));

        return ResponseEntity.ok(response);
    }

    @GetMapping("listarusuarios")
    public List<UsuariosDto> listarusuarios(){
        List<UsuariosDomain> lista = listarUsuariosUsecase.execute();

        return lista.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
