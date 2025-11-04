package com.alanhss.ClashZone.infra.presentation;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.usecases.usuario.AtualizarUsuarioUsecase;
import com.alanhss.ClashZone.core.usecases.usuario.BuscarUsuarioPorId;
import com.alanhss.ClashZone.core.usecases.usuario.CriarUsuarioUsecase;
import com.alanhss.ClashZone.core.usecases.usuario.ListarUsuariosUsecase;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.AtualizarUsuariosDto;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.UsuariosDto;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("clashzone/auth/")
@RequiredArgsConstructor
public class UsuarioController {

    private final CriarUsuarioUsecase criarUsuarioUsecase;
    private final ListarUsuariosUsecase listarUsuariosUsecase;
    private final AtualizarUsuarioUsecase atualizarUsuarioUsecase;
    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final UsuariosDtoMapper mapper;
    private final UsuariosAtualizarMapper atualizarMapper;

    @PostMapping("criarusuario")
    public ResponseEntity<String> criarUsuario(@RequestBody UsuariosDto usuariosDto){
        UsuariosDto dtoValidado = mapper.validarEPreparar(usuariosDto);
        UsuariosDomain novoUsuarioDomain = criarUsuarioUsecase.execute(mapper.toDomain(dtoValidado));

        return ResponseEntity.ok("Usuário criado com sucesso!");
    }

    @GetMapping("listarusuarios")
    public List<UsuariosDto> listarusuarios(){
        List<UsuariosDomain> lista = listarUsuariosUsecase.execute();

        return lista.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("atualizarusuario/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@PathVariable Long id, @RequestBody AtualizarUsuariosDto atualizarUsuariosDto) {
        Map<String, Object> response = new HashMap<>();

        AtualizarUsuariosDto dtoValidado = atualizarMapper.validarEPrepararAtualizacao(atualizarUsuariosDto);

        UsuariosDomain usuariosDomain = atualizarMapper.toDomain(id, dtoValidado);
        UsuariosDomain usuarioAtualizado = atualizarUsuarioUsecase.execute(id, usuariosDomain);
        response.put("Mensagem: ", "Usuário atualizado com sucesso!");
        response.put("Dados do usuário: ", mapper.toDto(usuarioAtualizado));

        return ResponseEntity.ok(response);
    }

    @GetMapping("userprofile/{id}")
    public UsuariosDto buscarUsuarioPorId(@PathVariable Long id){
        UsuariosDomain usuariosDomain = buscarUsuarioPorId.execute(id);
        UsuariosDto usuariosDto = mapper.toDto(usuariosDomain);

        return usuariosDto;
    }

}
