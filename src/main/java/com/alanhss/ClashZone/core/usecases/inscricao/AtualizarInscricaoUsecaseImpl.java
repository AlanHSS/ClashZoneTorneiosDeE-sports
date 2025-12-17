package com.alanhss.ClashZone.core.usecases.inscricao;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.StatusInscricao;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.exceptions.StatusInscricaoInvalidoException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.InscricaoTorneioGateway;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

public class AtualizarInscricaoUsecaseImpl implements AtualizarInscricaoUsecase{

    private final InscricaoTorneioGateway inscricaoTorneioGateway;
    private final TorneioGateway torneioGateway;
    private final EquipeGateway equipeGateway;

    public AtualizarInscricaoUsecaseImpl(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway, EquipeGateway equipeGateway) {
        this.inscricaoTorneioGateway = inscricaoTorneioGateway;
        this.torneioGateway = torneioGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public InscricaoTorneioDomain execute(Long id, InscricaoTorneioDomain inscricaoTorneioDomain, Long usuarioAutenticadoId, Role roleUsuario) {
        InscricaoTorneioDomain inscricaoExistente = inscricaoTorneioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "inscrição"));

        TorneioDomain torneio = torneioGateway.buscarPorId(inscricaoExistente.torneioId())
                .orElseThrow(() -> new NaoEncontradoPorIdException(inscricaoExistente.torneioId(), "torneio"));

        EquipeDomain equipe = equipeGateway.buscarPorId(inscricaoExistente.equipeId())
                .orElseThrow(() -> new NaoEncontradoPorIdException(inscricaoExistente.equipeId(), "equipe"));

        if (inscricaoTorneioDomain.statusInscricao() == null) {
            throw new CampoObrigatorioException("Status da inscrição");
        }

        validarPermissoes(
                inscricaoTorneioDomain.statusInscricao(),
                torneio.criadorId(),
                equipe.liderId(),
                usuarioAutenticadoId,
                roleUsuario
        );

        validarTransicaoStatus(inscricaoExistente.statusInscricao(), inscricaoTorneioDomain.statusInscricao());

        validarMotivoRecusa(inscricaoTorneioDomain.statusInscricao(), inscricaoTorneioDomain.motivoRecusa());

        String motivoRecusa = inscricaoTorneioDomain.statusInscricao() == StatusInscricao.RECUSADA
                ? inscricaoTorneioDomain.motivoRecusa()
                : null;

        InscricaoTorneioDomain inscricaoParaAtualizar = new InscricaoTorneioDomain(
                id,
                inscricaoExistente.torneioId(),
                inscricaoExistente.equipeId(),
                inscricaoTorneioDomain.statusInscricao(),
                motivoRecusa,
                inscricaoExistente.dataInscricao()
        );

        return inscricaoTorneioGateway.atualizarInscricao(id, inscricaoParaAtualizar);
    }

    private void validarPermissoes(StatusInscricao novoStatus, Long criadorTorneioId, Long liderEquipeId, Long usuarioAutenticadoId, Role roleUsuario) {
        boolean isAdmin = roleUsuario == Role.ADMIN;
        boolean isCriadorTorneio = criadorTorneioId.equals(usuarioAutenticadoId);
        boolean isLiderEquipe = liderEquipeId.equals(usuarioAutenticadoId);

        if (novoStatus == StatusInscricao.CANCELADA) {
            if (!isLiderEquipe && !isCriadorTorneio && !isAdmin) {
                throw new AcessoNegadoException("Apenas o líder da equipe, o criador do torneio ou um administrador podem cancelar a inscrição");
            }
        } else {
            if (!isCriadorTorneio && !isAdmin) {
                throw new AcessoNegadoException("Apenas o criador do torneio ou um administrador podem aprovar ou recusar inscrições");
            }
        }
    }

    private void validarTransicaoStatus(StatusInscricao statusAtual, StatusInscricao novoStatus) {
        if (statusAtual == StatusInscricao.APROVADA || statusAtual == StatusInscricao.RECUSADA) {
            throw new StatusInscricaoInvalidoException(statusAtual, novoStatus);
        }

        if (novoStatus == StatusInscricao.CANCELADA && statusAtual != StatusInscricao.PENDENTE) {
            throw new StatusInscricaoInvalidoException(statusAtual, novoStatus);
        }
    }

    private void validarMotivoRecusa(StatusInscricao novoStatus, String motivoRecusa) {
        if (novoStatus == StatusInscricao.RECUSADA) {
            if (motivoRecusa == null || motivoRecusa.trim().isEmpty()) {
                throw new CampoObrigatorioException("motivo recusa");
            }
        }
    }
}
