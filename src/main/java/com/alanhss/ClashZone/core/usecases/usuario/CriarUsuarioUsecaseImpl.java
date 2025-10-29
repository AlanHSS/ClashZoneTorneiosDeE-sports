package com.alanhss.ClashZone.core.usecases.usuario;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.EmailJaExisteException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.core.exceptions.NicknameJaExisteException;

import java.util.ArrayList;
import java.util.List;

public class CriarUsuarioUsecaseImpl implements CriarUsuarioUsecase{

    private final UsuariosGateway usuariosGateway;

    public CriarUsuarioUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(UsuariosDomain usuariosDomain) {

        List<String> camposFaltantes = new ArrayList<>();

        if (usuariosDomain.nomeDoUsuario() == null || usuariosDomain.nomeDoUsuario().trim().isEmpty()) {
            camposFaltantes.add("Nome do usuário");
        }

        if (usuariosDomain.nickname() == null || usuariosDomain.nickname().trim().isEmpty()) {
            camposFaltantes.add("Nickname");
        }

        if (usuariosDomain.emailDoUsuario() == null || usuariosDomain.emailDoUsuario().trim().isEmpty()) {
            camposFaltantes.add("E-mail do usuário");
        }

        if (usuariosDomain.senhaDoUsuario() == null || usuariosDomain.senhaDoUsuario().isEmpty()) {
            camposFaltantes.add("Senha do usuário");
        } else if (usuariosDomain.senhaDoUsuario().length() < 8) {
            camposFaltantes.add("Senha do usuário (mínimo 8 caracteres)");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        if (usuariosGateway.existeNickname(usuariosDomain.nickname())){
            throw new NicknameJaExisteException(usuariosDomain.nickname());
        }
        if (usuariosGateway.existeEmail(usuariosDomain.emailDoUsuario())){
            throw new EmailJaExisteException(usuariosDomain.emailDoUsuario());
        }

        return usuariosGateway.criarUsuario(usuariosDomain);
    }

}
