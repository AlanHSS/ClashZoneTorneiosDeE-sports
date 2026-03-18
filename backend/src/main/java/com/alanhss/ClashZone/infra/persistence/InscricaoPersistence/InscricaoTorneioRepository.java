package com.alanhss.ClashZone.infra.persistence.InscricaoPersistence;

import com.alanhss.ClashZone.core.enums.StatusInscricao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InscricaoTorneioRepository extends JpaRepository<InscricaoTorneioEntity, Long> {

    List<InscricaoTorneioEntity> findByTorneioIdId(Long torneioId);

    List<InscricaoTorneioEntity> findByEquipeIdId(Long equipeId);

    List<InscricaoTorneioEntity> findByTorneioIdIdAndStatusInscricao(Long torneioId, StatusInscricao status);

    Page<InscricaoTorneioEntity> findByTorneioIdId(Long torneioId, Pageable pageable);

    Page<InscricaoTorneioEntity> findByEquipeIdId(Long equipeId, Pageable pageable);

    Page<InscricaoTorneioEntity> findByTorneioIdIdAndStatusInscricao(Long torneioId, StatusInscricao status, Pageable pageable);

    Page<InscricaoTorneioEntity> findByEquipeIdIdAndStatusInscricao(Long equipeId, StatusInscricao status, Pageable pageable);

    @Query("SELECT i FROM InscricaoTorneioEntity i WHERE i.equipeId.id IN :equipeIds AND (:status IS NULL OR i.statusInscricao = :status)")
    Page<InscricaoTorneioEntity> findByEquipeIdsAndStatus(
            @Param("equipeIds") List<Long> equipeIds,
            @Param("status") StatusInscricao status,
            Pageable pageable
    );

    boolean existsByTorneioIdIdAndEquipeIdId(Long torneioId, Long equipeId);

    Optional<InscricaoTorneioEntity> findByTorneioIdIdAndEquipeIdId(Long torneioId, Long equipeId);

    @Query("SELECT COUNT(i) FROM InscricaoTorneioEntity i WHERE i.torneioId.id = :torneioId AND i.statusInscricao = 'APROVADA'")
    int contarInscricoesAprovadas(@Param("torneioId") Long torneioId);
}
