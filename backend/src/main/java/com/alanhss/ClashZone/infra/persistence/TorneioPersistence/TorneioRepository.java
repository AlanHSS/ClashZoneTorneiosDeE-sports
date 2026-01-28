package com.alanhss.ClashZone.infra.persistence.TorneioPersistence;

import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TorneioRepository extends JpaRepository<TorneioEntity, Long>, JpaSpecificationExecutor<TorneioEntity> {

    List<TorneioEntity> findByCriadorIdId(Long criadorId);

    List<TorneioEntity> findByStatusDoTorneio(StatusDoTorneio status);
}
