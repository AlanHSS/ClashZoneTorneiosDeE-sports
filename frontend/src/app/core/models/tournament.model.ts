import { Games, Plataforma, StatusDoTorneio } from './enums';

export interface TorneioDomain {
  id: number;
  nomeDoTorneio: string;
  descricaoDoTorneio?: string;
  inicioDoTorneio: string;
  jogoDoTorneio: Games;
  quantidadeDeEquipes: number;
  criadorId: number;
  statusDoTorneio: StatusDoTorneio;
  plataforma: Plataforma;
  dataCriacao: string;
}

export interface CriarTorneioDto {
  nomeDoTorneio: string;
  descricaoDoTorneio?: string;
  inicioDoTorneio: string;
  jogoDoTorneio: Games;
  quantidadeDeEquipes: number;
  statusDoTorneio: StatusDoTorneio;
  plataforma: Plataforma;
}

export interface AtualizarTorneioDto {
  nomeDoTorneio?: string;
  descricaoDoTorneio?: string;
  inicioDoTorneio?: string;
  jogoDoTorneio?: Games;
  quantidadeDeEquipes?: number;
  statusDoTorneio?: StatusDoTorneio;
  plataforma?: Plataforma;
}

export interface FiltroTorneioDto {
  nomeDoTorneio?: string;
  inicioDoTorneio?: string;
  jogoDoTorneio?: Games;
  statusDoTorneio?: StatusDoTorneio;
  plataforma?: Plataforma;
}
