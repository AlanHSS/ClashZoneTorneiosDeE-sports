package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

public class DeletarUsuarioPorIdUsecaseImpl implements DeletarUsuarioPorIdUsecase {

    private final UsuariosGateway usuariosGateway;

    public DeletarUsuarioPorIdUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public void execute(Long id, Long usuarioAutenticadoId, Role roleUsuario) {

        usuariosGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "usuário"));

        if (roleUsuario != Role.ADMIN && !id.equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Você só pode deletar sua própria conta");
        }

        usuariosGateway.deletarUsuario(id);
    }
}
