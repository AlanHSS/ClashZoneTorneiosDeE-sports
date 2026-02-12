import { Games } from './enums';

export interface EquipeDomain {
  id: number;
  nomeDaEquipe: string;
  liderId: number;
  jogo: Games;
  dataCriacao: string;
  inscrita: boolean;
}

export interface CriarEquipeDto {
  nomeDaEquipe: string;
  jogo: Games;
}

export interface AtualizarEquipeDto {
  nomeDaEquipe?: string;
  jogo?: Games;
  inscrita?: boolean;
}
