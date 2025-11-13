package com.alanhss.ClashZone.infra.gateway;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.CampoDuplicadoException;
import com.alanhss.ClashZone.core.gateway.AuthGateway;
import com.alanhss.ClashZone.infra.mappers.UsuariosMappers.UsuariosEntityMapper;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosRepository;
import com.alanhss.ClashZone.infra.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthRepositoryGateway implements AuthGateway {

    private final UsuariosRepository usuariosRepository;
    private final UsuariosEntityMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean existeNickname(String nickname) {
        return usuariosRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuariosRepository.existsByEmailDoUsuario(email);
    }

    @Override
    public Optional<UsuariosDomain> buscarPorEmail(String email) {
        return usuariosRepository.findByEmailDoUsuario(email)
                .map(mapper::toDomain);
    }

    @Override
    public AuthDomain registerUser(UsuariosDomain usuariosDomain) {

        UsuariosEntity novoUsuario = usuariosRepository.save(mapper.toEntity(usuariosDomain));

        novoUsuario.setSenhaDoUsuario(passwordEncoder.encode(usuariosDomain.senhaDoUsuario()));

        novoUsuario.setRole(com.alanhss.ClashZone.core.enums.Role.USER);

        UsuariosEntity usuarioSalvo = usuariosRepository.save(novoUsuario);

        String token =jwtService.generateToken(usuarioSalvo);

        return new AuthDomain(
                usuarioSalvo.getId(),
                usuarioSalvo.getNomeDoUsuario(),
                usuarioSalvo.getNickname(),
                usuarioSalvo.getEmailDoUsuario(),
                usuarioSalvo.getRole().name(),
                token
        );
    }

    @Override
    public AuthDomain loginUser(String email, String senha) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, senha)
            );

            UsuariosEntity usuario = usuariosRepository.findByEmailDoUsuario(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtService.generateToken(usuario);

            return new AuthDomain(
                    usuario.getId(),
                    usuario.getNomeDoUsuario(),
                    usuario.getNickname(),
                    usuario.getEmailDoUsuario(),
                    usuario.getRole().name(),
                    token
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }
    }
}
