package com.alanhss.ClashZone.infra.persistence.InscricaoPersistence;

import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.infra.persistence.EquipePersistence.EquipeEntity;
import com.alanhss.ClashZone.infra.persistence.TorneioPersistence.TorneioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes_torneio")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InscricaoTorneioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneio_id", nullable = false)
    private TorneioEntity torneioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id", nullable = false)
    private EquipeEntity equipeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_inscricao", nullable = false)
    private StatusInscricao statusInscricao;

    @Column(name = "motivo_recusa", columnDefinition = "TEXT")
    private String motivoRecusa;

    @Column(name = "data_inscricao", updatable = false, insertable = false)
    private LocalDateTime dataInscricao;
}
