package com.alanhss.ClashZone.core.usecases.auth;

import com.alanhss.ClashZone.core.domain.AuthDomain;

public interface LoginUsecase {

    public AuthDomain execute(AuthDomain.LoginData loginData);
}
