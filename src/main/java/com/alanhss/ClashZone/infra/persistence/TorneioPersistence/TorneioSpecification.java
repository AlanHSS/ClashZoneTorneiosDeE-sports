package com.alanhss.ClashZone.infra.persistence.TorneioPersistence;

import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Plataforma;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TorneioSpecification {

    private final TorneioRepository torneioRepository;

    public static Specification<TorneioEntity> comNome(String nome) {
        return (root, query, cb) ->
                nome == null ? null :
                        cb.like(cb.lower(root.get("nomeDoTorneio")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<TorneioEntity> comJogo(Games jogo) {
        return (root, query, cb) ->
                jogo == null ? null :
                        cb.equal(root.get("jogoDoTorneio"), Games.valueOf(String.valueOf(jogo)));
    }

    public static Specification<TorneioEntity> comPlataforma(Plataforma plataforma) {
        return (root, query, cb) ->
                plataforma == null ? null :
                        cb.equal(root.get("plataforma"), Plataforma.valueOf(String.valueOf(plataforma)));
    }

    public static Specification<TorneioEntity> comStatus(StatusDoTorneio status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("statusDoTorneio"), StatusDoTorneio.valueOf(String.valueOf(status)));
    }

    public static Specification<TorneioEntity> comDataInicio(LocalDateTime dataInicio) {
        return (root, query, cb) ->
                dataInicio == null ? null :
                        cb.greaterThanOrEqualTo(root.get("inicioDoTorneio"), dataInicio);
    }
}
