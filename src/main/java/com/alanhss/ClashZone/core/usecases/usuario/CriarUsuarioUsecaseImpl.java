package com.alanhss.ClashZone.core.usecases.usuario;
import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.EmailJaExisteException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.core.exceptions.NicknameJaExisteException;

public class CriarUsuarioUsecaseImpl implements CriarUsuarioUsecase{

    private final UsuariosGateway usuariosGateway;

    public CriarUsuarioUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(UsuariosDomain usuariosDomain) {

        if (usuariosGateway.existeNickname(usuariosDomain.nickname())){
            throw new NicknameJaExisteException(usuariosDomain.nickname());
        }
        if (usuariosGateway.existeEmail(usuariosDomain.emailDoUsuario())){
            throw new EmailJaExisteException(usuariosDomain.emailDoUsuario());
        }

        return usuariosGateway.criarUsuario(usuariosDomain);
    }

}
