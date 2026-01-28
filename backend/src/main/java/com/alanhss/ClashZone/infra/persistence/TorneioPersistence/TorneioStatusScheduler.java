package com.alanhss.ClashZone.infra.persistence.TorneioPersistence;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.enums.StatusDoTorneio;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TorneioStatusScheduler {

    private final TorneioGateway torneioGateway;

    public void atualizarStatusTorneiosAutomaticamente() {
        log.info("Iniciando verificação automática de status de torneios...");

        LocalDateTime agora = LocalDateTime.now();

        // Busca todos os torneios agendados
        List<TorneioDomain> torneiosAgendados = torneioGateway.listarTorneioPorStatus(StatusDoTorneio.AGENDADO);

        int torneiosAtualizados = 0;

        for (TorneioDomain torneio : torneiosAgendados) {
            // Se a data de início já passou ou é agora
            if (torneio.inicioDoTorneio().isBefore(agora) || torneio.inicioDoTorneio().isEqual(agora)) {

                // Atualiza para EM_ANDAMENTO
                TorneioDomain torneioAtualizado = new TorneioDomain(
                        torneio.id(),
                        torneio.nomeDoTorneio(),
                        torneio.descricaoDoTorneio(),
                        torneio.inicioDoTorneio(),
                        torneio.jogoDoTorneio(),
                        torneio.quantidadeDeEquipes(),
                        torneio.criadorId(),
                        StatusDoTorneio.EM_ANDAMENTO,
                        torneio.plataforma(),
                        torneio.dataCriacao()
                );

                torneioGateway.atualizarTorneio(torneio.id(), torneioAtualizado);
                torneiosAtualizados++;

                log.info("Torneio ID {} '{}' iniciado automaticamente às {}",
                        torneio.id(),
                        torneio.nomeDoTorneio(),
                        agora);
            }
        }

        log.info("Verificação concluída. {} torneio(s) atualizado(s) para EM_ANDAMENTO", torneiosAtualizados);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void finalizarTorneiosAntigos() {
        log.info("Verificando torneios antigos para finalização...");

        LocalDateTime umDiaAtras = LocalDateTime.now().minusDays(1);

        List<TorneioDomain> torneiosEmAndamento = torneioGateway.listarTorneioPorStatus(StatusDoTorneio.EM_ANDAMENTO);

        int torneiosFinalizados = 0;

        for (TorneioDomain torneio : torneiosEmAndamento) {
            // Se o torneio começou há mais de 1 dia
            if (torneio.inicioDoTorneio().isBefore(umDiaAtras)) {

                TorneioDomain torneioAtualizado = new TorneioDomain(
                        torneio.id(),
                        torneio.nomeDoTorneio(),
                        torneio.descricaoDoTorneio(),
                        torneio.inicioDoTorneio(),
                        torneio.jogoDoTorneio(),
                        torneio.quantidadeDeEquipes(),
                        torneio.criadorId(),
                        StatusDoTorneio.FINALIZADO,
                        torneio.plataforma(),
                        torneio.dataCriacao()
                );

                torneioGateway.atualizarTorneio(torneio.id(), torneioAtualizado);
                torneiosFinalizados++;

                log.info("Torneio ID {} '{}' finalizado automaticamente",
                        torneio.id(),
                        torneio.nomeDoTorneio());
            }
        }

        log.info("Finalização concluída. {} torneio(s) finalizado(s)", torneiosFinalizados);
    }
}
