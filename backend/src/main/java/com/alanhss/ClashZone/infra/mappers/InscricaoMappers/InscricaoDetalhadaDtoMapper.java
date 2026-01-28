package com.alanhss.ClashZone.infra.mappers.InscricaoMappers;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.infra.dtos.InscricaoDtos.InscricaoDetalhadaDto;
import com.alanhss.ClashZone.infra.persistence.InscricaoPersistence.InscricaoTorneioEntity;
import org.springframework.stereotype.Component;

@Component
public class InscricaoDetalhadaDtoMapper {

    public InscricaoDetalhadaDto toDto(InscricaoTorneioEntity entity) {
        return new InscricaoDetalhadaDto(
                entity.getId(),

                entity.getTorneioId().getId(),
                entity.getTorneioId().getNomeDoTorneio(),
                entity.getTorneioId().getInicioDoTorneio(),
                entity.getTorneioId().getJogoDoTorneio(),

                entity.getEquipeId().getId(),
                entity.getEquipeId().getNomeDaEquipe(),
                entity.getEquipeId().getLiderId(),

                entity.getStatusInscricao(),
                entity.getMotivoRecusa(),
                entity.getDataInscricao()
        );
    }

    public InscricaoDetalhadaDto fromDomain(InscricaoTorneioDomain domain,
                                            String nomeTorneio,
                                            java.time.LocalDateTime inicioTorneio,
                                            com.alanhss.ClashZone.core.enums.Games jogoTorneio,
                                            String nomeEquipe,
                                            Long liderId) {
        return new InscricaoDetalhadaDto(
                domain.id(),

                domain.torneioId(),
                nomeTorneio,
                inicioTorneio,
                jogoTorneio,

                domain.equipeId(),
                nomeEquipe,
                liderId,

                domain.statusInscricao(),
                domain.motivoRecusa(),
                domain.dataInscricao()
        );
    }
}
