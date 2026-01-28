package com.alanhss.ClashZone.core.usecases.auth;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;

public interface RegisterUsecase {

    public AuthDomain execute(AuthDomain.RegisterData registerData);
}
