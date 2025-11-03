package com.alanhss.ClashZone.infra.persistence.EquipePersistence;

import com.alanhss.ClashZone.core.enums.Games;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDaEquipe;

    private Long liderId;

    private Games jogo;

    @Column(name = "data_criacao", updatable = false, insertable = false)
    private LocalDateTime dataCriacao;

    private boolean inscrita;
}
