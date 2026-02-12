import { TipoMembro } from './enums';

export interface MembroEquipeDomain {
  id: number;
  equipeId: number;
  nickname: string;
  tipo: TipoMembro;
  rank?: string;
  dataAdicao: string;
}

export interface AdicionarMembroDto {
  nickname: string;
  tipo: TipoMembro;
  rank?: string;
}

export interface AtualizarMembroDto {
  id: number;
  nickname?: string;
  tipo?: TipoMembro;
  rank?: string;
}
