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

public class AtualizarMembrosEquipeUsecaseImpl implements AtualizarMembrosEquipeUsecase {

    private final MembroEquipeGateway membroEquipeGateway;
    private final EquipeGateway equipeGateway;

    public AtualizarMembrosEquipeUsecaseImpl(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        this.membroEquipeGateway = membroEquipeGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<MembroEquipeDomain> execute(Long equipeId, List<MembroEquipeDomain> membros, Long usuarioAutenticadoId, Role roleUsuario) {
        EquipeDomain equipe = equipeGateway.buscarPorId(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        if (roleUsuario != Role.ADMIN && !equipe.liderId().equals(usuarioAutenticadoId)) {
            throw new AcessoNegadoException("Apenas o líder da equipe ou um administrador podem atualizar membros");
        }

        if (membros == null || membros.isEmpty()) {
            throw new CampoObrigatorioException("É necessário informar pelo menos um membro para atualizar");
        }

        // Valida dados dos membros
        validarDadosMembros(membros);

        // Busca todos os membros atuais da equipe
        List<MembroEquipeDomain> membrosAtuais = membroEquipeGateway.buscarMembrosPorEquipe(equipeId);

        // Valida se os membros a serem atualizados existem e pertencem à equipe
        validarMembrosExistem(membros, membrosAtuais);

        // Valida nicknames duplicados (considerando membros que não estão sendo atualizados)
        validarNicknamesDuplicados(membros, membrosAtuais);

        // Valida quantidade de titulares e reservas após atualização
        validarQuantidadeMembros(equipe.jogo(), membros, membrosAtuais, equipeId);

        // Atualiza os membros
        List<MembroEquipeDomain> membrosAtualizados = new ArrayList<>();
        for (MembroEquipeDomain membroAtualizado : membros) {
            MembroEquipeDomain resultado = membroEquipeGateway.atualizarMembros(membroAtualizado.id(), membroAtualizado);
            membrosAtualizados.add(resultado);
        }

        return membrosAtualizados;
    }

    private void validarDadosMembros(List<MembroEquipeDomain> membros) {
        List<String> erros = new ArrayList<>();

        for (int i = 0; i < membros.size(); i++) {
            MembroEquipeDomain membro = membros.get(i);

            if (membro.id() == null) {
                erros.add("Membro " + (i + 1) + ": ID é obrigatório para atualização");
            }

            if (membro.nickname() != null && membro.nickname().trim().isEmpty()) {
                erros.add("Membro " + (i + 1) + ": Nickname não pode estar vazio");
            }
        }

        if (!erros.isEmpty()) {
            throw new CampoObrigatorioException(erros);
        }
    }

    private void validarMembrosExistem(List<MembroEquipeDomain> membrosParaAtualizar, List<MembroEquipeDomain> membrosAtuais) {
        Set<Long> idsAtuais = membrosAtuais.stream()
                .map(MembroEquipeDomain::id)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        List<Long> idsInvalidos = membrosParaAtualizar.stream()
                .map(MembroEquipeDomain::id)
                .filter(id -> !idsAtuais.contains(id))
                .toList();

        if (!idsInvalidos.isEmpty()) {
            throw new DadoInvalidoException(
                    "membros",
                    "Os seguintes IDs de membros não existem na equipe: " + idsInvalidos
            );
        }
    }

    private void validarNicknamesDuplicados(List<MembroEquipeDomain> membrosParaAtualizar, List<MembroEquipeDomain> membrosAtuais) {
        // IDs dos membros que estão sendo atualizados
        Set<Long> idsAtualizando = membrosParaAtualizar.stream()
                .map(MembroEquipeDomain::id)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        // Nicknames dos membros que NÃO estão sendo atualizados
        Set<String> nicknamesExistentes = membrosAtuais.stream()
                .filter(m -> !idsAtualizando.contains(m.id()))
                .map(m -> m.nickname().toLowerCase())
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        // Verifica duplicação entre os próprios membros sendo atualizados
        Set<String> nicknamesAtualizando = new HashSet<>();
        List<String> duplicados = new ArrayList<>();

        for (MembroEquipeDomain membro : membrosParaAtualizar) {
            if (membro.nickname() != null) {
                String nickname = membro.nickname().toLowerCase();

                // Verifica se já existe em outros membros
                if (nicknamesExistentes.contains(nickname)) {
                    duplicados.add(membro.nickname());
                }

                // Verifica duplicação dentro da própria lista de atualização
                if (!nicknamesAtualizando.add(nickname)) {
                    duplicados.add(membro.nickname());
                }
            }
        }

        if (!duplicados.isEmpty()) {
            throw new DadoInvalidoException(
                    "nicknames",
                    "Os seguintes nicknames já existem na equipe: " + String.join(", ", duplicados)
            );
        }
    }

    private void validarQuantidadeMembros(Games jogo, List<MembroEquipeDomain> membrosParaAtualizar,
                                          List<MembroEquipeDomain> membrosAtuais, Long equipeId) {
        // IDs dos membros que estão sendo atualizados
        Set<Long> idsAtualizando = membrosParaAtualizar.stream()
                .map(MembroEquipeDomain::id)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        // Conta membros atuais que NÃO estão sendo atualizados
        long titularesNaoAtualizados = membrosAtuais.stream()
                .filter(m -> !idsAtualizando.contains(m.id()))
                .filter(m -> m.tipo() == TipoMembro.TITULAR)
                .count();

        long reservasNaoAtualizadas = membrosAtuais.stream()
                .filter(m -> !idsAtualizando.contains(m.id()))
                .filter(m -> m.tipo() == TipoMembro.RESERVA)
                .count();

        // Conta novos titulares e reservas dos membros sendo atualizados
        long novosTitulares = membrosParaAtualizar.stream()
                .filter(m -> m.tipo() != null && m.tipo() == TipoMembro.TITULAR)
                .count();

        long novosReservas = membrosParaAtualizar.stream()
                .filter(m -> m.tipo() != null && m.tipo() == TipoMembro.RESERVA)
                .count();

        // Total após atualização
        long totalTitulares = titularesNaoAtualizados + novosTitulares;
        long totalReservas = reservasNaoAtualizadas + novosReservas;

        // Valida titulares
        if (totalTitulares > jogo.getJogadoresTitulares()) {
            throw new DadoInvalidoException(
                    "titulares",
                    String.format("O jogo %s permite no máximo %d jogadores titulares. Total após atualização: %d",
                            jogo.getNomeExibicao(),
                            jogo.getJogadoresTitulares(),
                            totalTitulares)
            );
        }

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
                    String.format("O jogo %s permite no máximo %d jogadores reservas. Total após atualização: %d",
                            jogo.getNomeExibicao(),
                            jogo.getJogadoresReservas(),
                            totalReservas)
            );
        }
    }
}
