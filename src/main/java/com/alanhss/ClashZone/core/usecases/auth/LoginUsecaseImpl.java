package com.alanhss.ClashZone.core.usecases.auth;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.gateway.AuthGateway;

import java.util.ArrayList;
import java.util.List;

public class LoginUsecaseImpl implements LoginUsecase{

    private final AuthGateway authGateway;

    public LoginUsecaseImpl(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    @Override
    public AuthDomain execute(AuthDomain.LoginData loginData) {

        List<String> camposFaltantes = new ArrayList<>();

        if (loginData.emailDoUsuario() == null || loginData.emailDoUsuario().trim().isEmpty()) {
            camposFaltantes.add("E-mail do usuário");
        }

        if (loginData.senhaDoUsuario() == null || loginData.senhaDoUsuario().isEmpty()) {
            camposFaltantes.add("Senha do usuário");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

        String emailNormalizado = loginData.emailDoUsuario().toLowerCase().trim();

        return authGateway.loginUser(emailNormalizado, loginData.senhaDoUsuario());
    }
}
