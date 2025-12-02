package com.alanhss.ClashZone.core.domain;

import com.alanhss.ClashZone.core.enums.TipoMembro;

import java.time.LocalDateTime;

public record MembroEquipeDomain(Long id,
                                 Long equipeId,
                                 String nickname,
                                 TipoMembro tipo,
                                 String rank,
                                 LocalDateTime dataAdicao) {
}
