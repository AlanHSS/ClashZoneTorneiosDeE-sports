package com.alanhss.ClashZone.infra.persistence.UsuariosPersistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmailDoUsuario(String emailDoUsuario);

    Optional<UsuariosEntity> findByEmailDoUsuario(String emailDoUsuario);
}
