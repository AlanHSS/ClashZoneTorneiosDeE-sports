package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.enums.Games;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.core.enums.TipoMembro;
import com.alanhss.ClashZone.core.exceptions.AcessoNegadoException;
import com.alanhss.ClashZone.core.exceptions.CampoObrigatorioException;
import com.alanhss.ClashZone.core.exceptions.DadoInvalidoException;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdicionarMembrosEquipeUsecaseImpl implements AdicionarMembrosEquipeUsecase{

    private final MembroEquipeGateway membroEquipeGateway;
    private final EquipeGateway equipeGateway;

    public AdicionarMembrosEquipeUsecaseImpl(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        this.membroEquipeGateway = membroEquipeGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<MembroEquipeDomain> execute(Long equipeId, List<MembroEquipeDomain> membros, Long usuarioAutenticadoId, Role roleUsuario) {
        EquipeDomain equipe = equipeGateway.buscarPorId(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipe.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem adicionar membros");
        }
        if (membros == null || membros.isEmpty()) {
            throw new CampoObrigatorioException("É necessário informar pelo menos um membro");
        }

        validarDadosMembros(membros);

        validarNicknamesDuplicados(membros);

        List<MembroEquipeDomain> membrosExistentes = membroEquipeGateway.buscarMembrosPorEquipe(equipeId);

        validarNicknamesJaExistentes(membros, membrosExistentes);

        validarQuantidadeMembros(equipe.jogo(), membros, membrosExistentes);

        return membroEquipeGateway.adicionarMembros(membros);
    }

    private void validarDadosMembros(List<MembroEquipeDomain> membros) {
        List<String> erros = new ArrayList<>();

        for (int i = 0; i < membros.size(); i++) {
            MembroEquipeDomain membro = membros.get(i);

            if (membro.nickname() == null || membro.nickname().trim().isEmpty()) {
                erros.add("Membro " + (i + 1) + ": Nickname é obrigatório");
            }

            if (membro.tipo() == null) {
                erros.add("Membro " + (i + 1) + ": Tipo é obrigatório");
            }
        }

        if (!erros.isEmpty()) {
            throw new CampoObrigatorioException(erros);
        }
    }

    private void validarNicknamesDuplicados(List<MembroEquipeDomain> membros) {
        Set<String> nicknames = new HashSet<>();
        List<String> duplicados = new ArrayList<>();

        for (MembroEquipeDomain membro : membros) {
            String nickname = membro.nickname().toLowerCase();
            if (!nicknames.add(nickname)) {
                duplicados.add(membro.nickname());
            }
        }

        if (!duplicados.isEmpty()) {
            throw new DadoInvalidoException(
                    "nicknames",
                    "Nicknames duplicados na mesma requisição: " + String.join(", ", duplicados)
            );
        }
    }

    private void validarNicknamesJaExistentes(List<MembroEquipeDomain> novosMembros, List<MembroEquipeDomain> membrosExistentes) {
        Set<String> nicknamesExistentes = membrosExistentes.stream()
                .map(m -> m.nickname().toLowerCase())
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        List<String> duplicados = novosMembros.stream()
                .map(MembroEquipeDomain::nickname)
                .filter(nick -> nicknamesExistentes.contains(nick.toLowerCase()))
                .toList();

        if (!duplicados.isEmpty()) {
            throw new DadoInvalidoException(
                    "nicknames",
                    "Os seguintes nicknames já existem na equipe: " + String.join(", ", duplicados)
            );
        }
    }

    private void validarQuantidadeMembros(Games jogo, List<MembroEquipeDomain> novosMembros, List<MembroEquipeDomain> membrosExistentes) {

        // Conta titulares existentes
        long titularesExistentes = membrosExistentes.stream()
                .filter(m -> m.tipo() == TipoMembro.TITULAR)
                .count();

        // Conta novos titulares
        long novosTitulares = novosMembros.stream()
                .filter(m -> m.tipo() == TipoMembro.TITULAR)
                .count();

        // Total de titulares após adicionar
        long totalTitulares = titularesExistentes + novosTitulares;

        // Valida titulares
        if (totalTitulares > jogo.getJogadoresTitulares()) {
            throw new DadoInvalidoException(
                    "titulares",
                    String.format("O jogo %s permite no máximo %d jogadores titulares. Existentes: %d, Tentando adicionar: %d",
                            jogo.getNomeExibicao(),
                            jogo.getJogadoresTitulares(),
                            titularesExistentes,
                            novosTitulares)
            );
        }

        // Conta reservas existentes
        long reservasExistentes = membrosExistentes.stream()
                .filter(m -> m.tipo() == TipoMembro.RESERVA)
                .count();

        // Conta novos reservas
        long novosReservas = novosMembros.stream()
                .filter(m -> m.tipo() == TipoMembro.RESERVA)
                .count();

        // Total de reservas após adicionar
        long totalReservas = reservasExistentes + novosReservas;

        // Valida se o jogo permite reservas
        if (novosReservas > 0 && !jogo.isPermiteReservas()) {
            throw new DadoInvalidoException(
                    "reservas",
                    "O jogo " + jogo.getNomeExibicao() + " não permite jogadores reservas"
            );
        }

        // Valida quantidade de reservas
        if (totalReservas > jogo.getJogadoresReservas()) {
            throw new DadoInvalidoException(
                    "reservas",
                    String.format("O jogo %s permite no máximo %d jogadores reservas. Existentes: %d, Tentando adicionar: %d",
                            jogo.getNomeExibicao(),
                            jogo.getJogadoresReservas(),
                            reservasExistentes,
                            novosReservas)
            );
        }
    }
}
