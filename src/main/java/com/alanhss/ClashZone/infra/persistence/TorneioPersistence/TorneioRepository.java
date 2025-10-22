package com.alanhss.ClashZone.infra.persistence.TorneioPersistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TorneioRepository extends JpaRepository<TorneioEntity, Long>, JpaSpecificationExecutor<TorneioEntity> {
}
