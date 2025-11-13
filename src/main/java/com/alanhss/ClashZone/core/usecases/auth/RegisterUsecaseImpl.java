package com.alanhss.ClashZone.core.usecases.auth;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.CampoDuplicadoException;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.gateway.AuthGateway;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

import java.util.ArrayList;
import java.util.List;

public class RegisterUsecaseImpl implements RegisterUsecase{

    private final AuthGateway authGateway;

    public RegisterUsecaseImpl(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    @Override
    public AuthDomain execute(AuthDomain.RegisterData registerData) {

        List<String> camposFaltantes = new ArrayList<>();

        if (registerData.nomeDoUsuario() == null || registerData.nomeDoUsuario().trim().isEmpty()) {
            camposFaltantes.add("Nome do usuário");
        }

        if (registerData.nickname() == null || registerData.nickname().trim().isEmpty()) {
            camposFaltantes.add("Nickname");
        }

        if (registerData.emailDoUsuario() == null || registerData.emailDoUsuario().trim().isEmpty()) {
            camposFaltantes.add("E-mail do usuário");
        }

        if (registerData.senhaDoUsuario() == null || registerData.senhaDoUsuario().isEmpty()) {
            camposFaltantes.add("Senha do usuário");
        } else if (registerData.senhaDoUsuario().length() < 8) {
            camposFaltantes.add("Senha do usuário (mínimo 8 caracteres)");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        if (authGateway.existeNickname(registerData.nickname())) {
            throw new CampoDuplicadoException("nickname", registerData.nickname());
        }

        if (authGateway.existeEmail(registerData.emailDoUsuario())) {
            throw new CampoDuplicadoException("email", registerData.emailDoUsuario());
        }

        String emailNormalizado = registerData.emailDoUsuario().toLowerCase().trim();

        UsuariosDomain novoUsuario = new UsuariosDomain(
                null,
                registerData.nomeDoUsuario(),
                registerData.nickname(),
                emailNormalizado,
                registerData.senhaDoUsuario(),
                null,
                null
        );

        return authGateway.registerUser(novoUsuario);
    }

}
