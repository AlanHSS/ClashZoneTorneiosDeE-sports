package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.EmailJaExisteException;
import com.alanhss.ClashZone.core.exceptions.NicknameJaExisteException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;

import java.util.ArrayList;
import java.util.List;

public class AtualizarUsuarioUsecaseImpl implements AtualizarUsuarioUsecase{

    private final UsuariosGateway usuariosGateway;

    public AtualizarUsuarioUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain) {
        usuariosGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id));

        List<String> camposInvalidos = new ArrayList<>();

        if (usuariosDomain.nomeDoUsuario() != null && usuariosDomain.nomeDoUsuario().trim().isEmpty()) {
            camposInvalidos.add("Nome do usuario não pode estar vazio");
        }

        if (usuariosDomain.emailDoUsuario() != null && usuariosDomain.emailDoUsuario().trim().isEmpty()) {
            camposInvalidos.add("E-mail do usuário não pode estar vazio");
        }

        if (usuariosDomain.senhaDoUsuario() != null) {
            if (usuariosDomain.senhaDoUsuario().isEmpty()) {
                camposInvalidos.add("Senha do usuário não pode estar vazia");
            } else if (usuariosDomain.senhaDoUsuario().length() < 8) {
                camposInvalidos.add("Senha do usuário deve ter no mínimo 8 caracteres");
            }
        }

        if (!camposInvalidos.isEmpty()) {
            throw new CampoObrigatorioException(camposInvalidos);
        }

        if (usuariosGateway.existeEmail(usuariosDomain.emailDoUsuario())){
            throw new EmailJaExisteException(usuariosDomain.emailDoUsuario());
        }

        return usuariosGateway.atualizarUsuario(id, usuariosDomain);
    }
}
