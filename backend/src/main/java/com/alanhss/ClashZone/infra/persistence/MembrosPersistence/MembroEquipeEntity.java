package com.alanhss.ClashZone.infra.persistence.MembrosPersistence;

import com.alanhss.ClashZone.core.enums.TipoMembro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "membros_equipe")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MembroEquipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipe_id", nullable = false)
    private Long equipeId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMembro tipo;

    @Column(length = 50)
    private String rank;

    @Column(name = "data_adicao", updatable = false, insertable = false)
    private LocalDateTime dataAdicao;
}
