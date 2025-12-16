package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.enums.TipoMembro;
import com.alanhss.ClashZone.core.exceptions.*;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.ArrayList;
import java.util.List;

public class CriarInscricaoTorneioUsecaseImpl implements CriarInscricaoTorneioUsecase{

    private final InscricaoTorneioGateway inscricaoTorneioGateway;
    private final TorneioGateway torneioGateway;
    private final EquipeGateway equipeGateway;
    private final MembroEquipeGateway membroEquipeGateway;

    public CriarInscricaoTorneioUsecaseImpl(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway, EquipeGateway equipeGateway, MembroEquipeGateway membroEquipeGateway) {
        this.inscricaoTorneioGateway = inscricaoTorneioGateway;
        this.torneioGateway = torneioGateway;
        this.equipeGateway = equipeGateway;
        this.membroEquipeGateway = membroEquipeGateway;
    }

    @Override
    public InscricaoTorneioDomain execute(InscricaoTorneioDomain inscricaoTorneioDomain, Long usuarioAutenticadoId) {
        validarCamposObrigatorios(inscricaoTorneioDomain);

        TorneioDomain torneio = torneioGateway.buscarPorId(inscricaoTorneioDomain.torneioId())
                .orElseThrow(() -> new NaoEncontradoPorIdException(inscricaoTorneioDomain.torneioId(), "torneio"));

        EquipeDomain equipe = equipeGateway.buscarPorId(inscricaoTorneioDomain.equipeId())
                .orElseThrow(() -> new NaoEncontradoPorIdException(inscricaoTorneioDomain.equipeId(), "equipe"));

        if (!equipe.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe pode inscrevê-la em torneios");
        }

        validarStatusTorneio(torneio);

        if (inscricaoTorneioGateway.existeInscricao(torneio.id(), equipe.id())) {
            throw new DadoInvalidoException("inscrição", "Esta equipe já está inscrita neste torneio");
        }

        validarJogoCompativel(equipe.jogo(), torneio.jogoDoTorneio());

        validarVagasDisponiveis(torneio);

        validarNumeroMinimoJogadores(equipe, torneio.jogoDoTorneio());

        InscricaoTorneioDomain novaInscricao = new InscricaoTorneioDomain(
                null,
                inscricaoTorneioDomain.torneioId(),
                inscricaoTorneioDomain.equipeId(),
                StatusInscricao.PENDENTE,
                null,
                null
        );

        return inscricaoTorneioGateway.criarInscricao(novaInscricao);
    }

    private void validarCamposObrigatorios(InscricaoTorneioDomain inscricaoTorneioDomain) {
        List<String> camposFaltantes = new ArrayList<>();

        if (inscricaoTorneioDomain.torneioId() == null) {
            camposFaltantes.add("ID do torneio");
        }

        if (inscricaoTorneioDomain.equipeId() == null) {
            camposFaltantes.add("ID da equipe");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new CampoObrigatorioException(camposFaltantes);
        }

    }

    private void validarStatusTorneio(TorneioDomain torneio) {
        if (torneio.statusDoTorneio() != StatusDoTorneio.AGENDADO) {
            throw new TorneioNaoDisponivelException(torneio.id(), torneio.statusDoTorneio());
        }
    }

    private void validarJogoCompativel(Games jogoEquipe, Games jogoTorneio) {
        if (jogoEquipe != jogoTorneio) {
            throw new JogoIncompativelException(jogoEquipe, jogoTorneio);
        }
    }

    private void validarVagasDisponiveis(TorneioDomain torneio) {
        int inscricoesAprovadas = inscricaoTorneioGateway.contarInscricoesAprovadas(torneio.id());

        if (inscricoesAprovadas >= torneio.quantidadeDeEquipes()) {
            throw new TorneioSemVagasException(
                    torneio.id(),
                    inscricoesAprovadas,
                    torneio.quantidadeDeEquipes()
            );
        }
    }

    private void validarNumeroMinimoJogadores(EquipeDomain equipe, Games jogo) {
        List<MembroEquipeDomain> membros = membroEquipeGateway.buscarMembrosPorEquipe(equipe.id());

        long titulares = membros.stream()
                .filter(m -> m.tipo() == TipoMembro.TITULAR)
                .count();

        int minimoNecessario = jogo.getMinimoJogadores();

        if (titulares < minimoNecessario) {
            throw new EquipeIncompletaException(
                    equipe.id(),
                    (int) titulares,
                    minimoNecessario,
                    jogo
            );
        }
    }
}
