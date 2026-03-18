package com.alanhss.ClashZone.infra.persistence.EquipePersistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipeRepository extends JpaRepository<EquipeEntity, Long> {

    List<EquipeEntity> findByLiderId(Long liderId);

    Page<EquipeEntity> findByLiderId(Long liderId, Pageable pageable);
}
