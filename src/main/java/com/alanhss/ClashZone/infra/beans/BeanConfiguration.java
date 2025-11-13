package com.alanhss.ClashZone.infra.beans;

import com.alanhss.ClashZone.core.gateway.AuthGateway;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.core.usecases.auth.LoginUsecase;
import com.alanhss.ClashZone.core.usecases.auth.LoginUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.auth.RegisterUsecase;
import com.alanhss.ClashZone.core.usecases.auth.RegisterUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.equipe.CriarEquipeUsecase;
import com.alanhss.ClashZone.core.usecases.equipe.CriarEquipeUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesUsecase;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.torneio.*;
import com.alanhss.ClashZone.core.usecases.usuario.*;
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

    @Bean
    public FiltrosTorneioUsecase filtrartorneio(TorneioGateway torneioGateway){
        return new FiltrosTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public AtualizarTorneioUsecase atualizartorneio(TorneioGateway torneioGateway){
        return new AtualizarTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public BuscarTorneioPorId buscartorneioporid(TorneioGateway torneioGateway){
        return new BuscarTorneioPorIdImpl(torneioGateway);
    }

    @Bean
    public ListarUsuariosUsecase listarusuarios(UsuariosGateway usuariosGateway){
        return new ListarUsuariosUsecaseImpl(usuariosGateway);
    }

    @Bean
    public AtualizarUsuarioUsecase atualizarusuarios(UsuariosGateway usuariosGateway){
        return new AtualizarUsuarioUsecaseImpl(usuariosGateway);
    }

    @Bean
    public BuscarUsuarioPorId buscarUsuarioPorId(UsuariosGateway usuariosGateway){
        return new BuscarUsuarioPorIdImpl(usuariosGateway);
    }

    @Bean
    public CriarEquipeUsecase criarequipe(EquipeGateway equipeGateway){
        return new CriarEquipeUsecaseImpl(equipeGateway);
    }

    @Bean
    public ListarEquipesUsecase listarEquipesUsecase(EquipeGateway equipeGateway){
        return new ListarEquipesUsecaseImpl(equipeGateway);
    }

    @Bean
    public RegisterUsecase registerUsecase(AuthGateway authGateway){
        return new RegisterUsecaseImpl(authGateway);
    }

    @Bean
    public LoginUsecase loginUsecase(AuthGateway authGateway) {
        return new LoginUsecaseImpl(authGateway);
    }
}
