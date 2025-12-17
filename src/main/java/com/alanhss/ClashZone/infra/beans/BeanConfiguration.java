package com.alanhss.ClashZone.infra.beans;

import com.alanhss.ClashZone.core.domain.InscricaoTorneioDomain;
import com.alanhss.ClashZone.core.gateway.*;
import com.alanhss.ClashZone.core.usecases.auth.LoginUsecase;
import com.alanhss.ClashZone.core.usecases.auth.LoginUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.auth.RegisterUsecase;
import com.alanhss.ClashZone.core.usecases.auth.RegisterUsecaseImpl;
import com.alanhss.ClashZone.core.usecases.equipe.*;
import com.alanhss.ClashZone.core.usecases.inscricao.*;
import com.alanhss.ClashZone.core.usecases.membro.*;
import com.alanhss.ClashZone.core.usecases.torneio.*;
import com.alanhss.ClashZone.core.usecases.usuario.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // TORNEIOS

    @Bean
    public CriarTorneioUsecase criartorneio(TorneioGateway torneioGateway) {
        return new CriarTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public ListarTorneiosUsecase listartorneio(TorneioGateway torneioGateway) {
        return new ListarTorneiosUsecaseImpl(torneioGateway);
    }

    @Bean
    public FiltrosTorneioUsecase filtrartorneio(TorneioGateway torneioGateway) {
        return new FiltrosTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public AtualizarTorneioUsecase atualizartorneio(TorneioGateway torneioGateway) {
        return new AtualizarTorneioUsecaseImpl(torneioGateway);
    }

    @Bean
    public BuscarTorneioPorIdUsecase buscarTorneioPorIdUsecase(TorneioGateway torneioGateway) {
        return new BuscarTorneioPorIdUsecaseImpl(torneioGateway);
    }

    @Bean
    public ListarTorneiosPorCriador listarTorneiosPorCriador(TorneioGateway torneioGateway) {
        return new ListarTorneiosPorCriadorImpl(torneioGateway);
    }

    // USUÁRIOS

    @Bean
    public ListarUsuariosUsecase listarusuarios(UsuariosGateway usuariosGateway) {
        return new ListarUsuariosUsecaseImpl(usuariosGateway);
    }

    @Bean
    public AtualizarUsuarioUsecase atualizarusuarios(UsuariosGateway usuariosGateway) {
        return new AtualizarUsuarioUsecaseImpl(usuariosGateway);
    }

    @Bean
    public BuscarUsuarioPorIdUsecase buscarUsuarioPorId(UsuariosGateway usuariosGateway) {
        return new BuscarUsuarioPorIdUsecaseImpl(usuariosGateway);
    }

    @Bean
    public DeletarUsuarioPorIdUsecase deletarUsuarioPorIdUsecase(UsuariosGateway usuariosGateway) {
        return new DeletarUsuarioPorIdUsecaseImpl(usuariosGateway);
    }

    // EQUIPES

    @Bean
    public CriarEquipeUsecase criarequipe(EquipeGateway equipeGateway) {
        return new CriarEquipeUsecaseImpl(equipeGateway);
    }

    @Bean
    public ListarEquipesUsecase listarEquipesUsecase(EquipeGateway equipeGateway) {
        return new ListarEquipesUsecaseImpl(equipeGateway);
    }

    @Bean
    public AtualizarEquipeUsecase atualizarEquipe(EquipeGateway equipeGateway) {
        return new AtualizarEquipeUsecaseImpl(equipeGateway);
    }

    @Bean
    public BuscarEquipePorIdUsecase buscarEquipePorId(EquipeGateway equipeGateway) {
        return new BuscarEquipePorIdUsecaseImpl(equipeGateway);
    }

    @Bean
    public DeletarEquipePorIdUsecase deletarEquipePorIdUsecase(EquipeGateway equipeGateway){
        return new DeletarEquipePorIdUsecaseImpl(equipeGateway);
    }

    @Bean
    public ListarEquipesPorLiderUsecase listarEquipesPorLiderUsecase(EquipeGateway equipeGateway){
        return new ListarEquipesPorLiderUsecaseImpl(equipeGateway);
    }

    // MEMRBOS_EQUIPE

    @Bean
    public AdicionarMembrosEquipeUsecase adicionarMembrosEquipe(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway){
        return new AdicionarMembrosEquipeUsecaseImpl(membroEquipeGateway, equipeGateway);
    }

    @Bean
    public ListarMembrosPorEquipeUsecase listarMembrosPorEquipeUsecase(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway){
        return new ListarMembrosPorEquipeUsecaseImpl(membroEquipeGateway, equipeGateway);
    }

    @Bean
    public DeletarMembroEquipeUsecase deletarMembroEquipeUsecase(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        return new DeletarMembroEquipeUsecaseImpl(membroEquipeGateway, equipeGateway);
    }

    @Bean
    public AtualizarMembrosEquipeUsecase atualizarMembrosEquipeUsecase(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        return new AtualizarMembrosEquipeUsecaseImpl(membroEquipeGateway, equipeGateway);
    }

    // INSCRIÇÃO_TORNEIO

    @Bean
    public CriarInscricaoTorneioUsecase criarInscricaoTorneioUsecase(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway, EquipeGateway equipeGateway, MembroEquipeGateway membroEquipeGateway) {
        return new CriarInscricaoTorneioUsecaseImpl(inscricaoTorneioGateway, torneioGateway, equipeGateway, membroEquipeGateway
        );
    }

    @Bean
    public ListarInscricoesPorTorneioUsecase listarInscricoesPorTorneioUsecase(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway) {
        return new ListarInscricoesPorTorneioUsecaseImpl(inscricaoTorneioGateway, torneioGateway);
    }

    @Bean
    public ListarInscricoesPorEquipeUsecase listarInscricoesPorEquipeUsecase(InscricaoTorneioGateway inscricaoTorneioGateway, EquipeGateway equipeGateway) {
        return new ListarInscricoesPorEquipeUsecaseImpl(inscricaoTorneioGateway, equipeGateway);
    }

    @Bean
    public AtualizarInscricaoUsecase atualizarInscricaoTorneioUsecase(InscricaoTorneioGateway inscricaoTorneioGateway, TorneioGateway torneioGateway, EquipeGateway equipeGateway) {
        return new AtualizarInscricaoUsecaseImpl(inscricaoTorneioGateway, torneioGateway, equipeGateway);
    }

    @Bean
    public BuscarInscricaoPorIdUsecase buscarInscricaoPorIdUsecase(InscricaoTorneioGateway inscricaoTorneioGateway) {
        return new BuscarInscricaoPorIdUsecaseImpl(inscricaoTorneioGateway);
    }

    // AUTENTICAÇÃO

    @Bean
    public RegisterUsecase registerUsecase(AuthGateway authGateway) {
        return new RegisterUsecaseImpl(authGateway);
    }

    @Bean
    public LoginUsecase loginUsecase(AuthGateway authGateway) {
        return new LoginUsecaseImpl(authGateway);
    }
}
