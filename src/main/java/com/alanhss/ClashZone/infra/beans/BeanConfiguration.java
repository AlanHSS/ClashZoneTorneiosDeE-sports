package com.alanhss.ClashZone.infra.beans;

import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecase;
import com.alanhss.ClashZone.core.usecases.CriarTorneioUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.ListarTorneiosUsecase;
import com.alanhss.ClashZone.core.usecases.ListarTorneiosUsecaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CriarTorneioUsecase criartorneio(TorneioGateway torneioGateway){
        return new CriarTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public ListarTorneiosUsecase listartorneio(TorneioGateway torneioGateway){
        return new ListarTorneiosUsecaseImpl(torneioGateway);
    }
}
