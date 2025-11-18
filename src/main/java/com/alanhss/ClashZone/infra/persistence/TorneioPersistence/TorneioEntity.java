package com.alanhss.ClashZone.infra.persistence.TorneioPersistence;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import com.alanhss.ClashZone.infra.persistence.UsuariosPersistence.UsuariosEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="torneios")
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private UsuariosEntity criadorId;

    @Enumerated(EnumType.STRING)
    private StatusDoTorneio statusDoTorneio;

    @Enumerated(EnumType.STRING)
    private Plataforma plataforma;

    @Column(name = "data_criacao", updatable = false, insertable = false)
    private LocalDateTime dataCriacao;

}