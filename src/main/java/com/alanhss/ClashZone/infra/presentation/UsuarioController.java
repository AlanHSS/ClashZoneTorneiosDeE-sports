package com.alanhss.ClashZone.infra.presentation;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.usecases.usuario.*;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.AtualizarUsuariosDto;
import com.alanhss.ClashZone.infra.dtos.UsuariosDtos.UsuariosDto;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosAtualizarMapper;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosDtoMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("clashzone/usuarios/")
@RequiredArgsConstructor
public class UsuarioController {

    private final ListarUsuariosUsecase listarUsuariosUsecase;
    private final AtualizarUsuarioUsecase atualizarUsuarioUsecase;
    private final BuscarUsuarioPorIdUsecase buscarUsuarioPorIdUsecase;
    private final DeletarUsuarioPorIdUsecase deletarUsuarioPorIdUsecase;
    private final UsuariosDtoMapper mapper;
    private final UsuariosAtualizarMapper atualizarMapper;

    private UsuariosEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsuariosEntity) {
            return (UsuariosEntity) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listartodosusuarios")
    public List<UsuariosDto> listarTodosUsuarios(){
        List<UsuariosDomain> lista = listarUsuariosUsecase.execute();

        return lista.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("atualizarusuario/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@PathVariable Long id, @RequestBody AtualizarUsuariosDto atualizarUsuariosDto) {
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        AtualizarUsuariosDto dtoValidado = atualizarMapper.validarEPrepararAtualizacao(atualizarUsuariosDto);

        UsuariosDomain usuariosDomain = atualizarMapper.toDomain(id, dtoValidado);
        UsuariosDomain usuarioAtualizado = atualizarUsuarioUsecase.execute(id, usuariosDomain, usuarioAutenticadoId, roleUsuario);
        response.put("Mensagem: ", "Usuário atualizado com sucesso!");
        response.put("Dados do usuário: ", mapper.toDto(usuarioAtualizado));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("userprofile/{id}")
    public UsuariosDto buscarUsuarioPorId(@PathVariable Long id){
        UsuariosDomain usuariosDomain = buscarUsuarioPorIdUsecase.execute(id);
        UsuariosDto usuariosDto = mapper.toDto(usuariosDomain);

        return usuariosDto;
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("deletarusuario/{id}")
    public ResponseEntity<Map<String, Object>> deletarUsuario(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        Long usuarioAutenticadoId = getUsuarioAutenticado().getId();
        Role roleUsuario = getUsuarioAutenticado().getRole();

        deletarUsuarioPorIdUsecase.execute(id, usuarioAutenticadoId, roleUsuario);

        response.put("Mensagem", "Usuário deletado com sucesso!");
        response.put("Id deletado", id);

        return ResponseEntity.ok(response);
    }

}
