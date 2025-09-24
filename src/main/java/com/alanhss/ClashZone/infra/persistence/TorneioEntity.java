package com.alanhss.ClashZone.infra.persistence;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="Torneios")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TorneioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDoTorneio;

    private String descricaoDoTorneio;

    private LocalDateTime inicioDoTorneio;

    @Enumerated(EnumType.STRING)
    private Games jogoDoTorneio;

    private Integer quantidadeDeEquipes;

    private String criadorDoTorneio;

    @Enumerated(EnumType.STRING)
    private StatusDoTorneio statusDoTorneio;

    @Enumerated(EnumType.STRING)
    private Plataforma plataforma;

}