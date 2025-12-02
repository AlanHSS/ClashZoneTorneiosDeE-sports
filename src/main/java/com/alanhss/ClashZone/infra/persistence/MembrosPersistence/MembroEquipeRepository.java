package com.alanhss.ClashZone.infra.persistence.MembrosPersistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembroEquipeRepository extends JpaRepository<MembroEquipeEntity, Long> {

    List<MembroEquipeEntity> findByEquipeId(Long equipeId);

    void deleteByEquipeId(Long equipeId);
}
