import { StatusInscricao } from './enums';

// Domain completo da inscrição
export interface InscricaoDomain {
  id: number;
  torneioId: number;
  equipeId: number;
  statusInscricao: StatusInscricao;
  dataInscricao: string;
}

// DTO para criar inscrição
export interface CriarInscricaoDto {
  torneioId: number;
  equipeId: number;
}

// DTO para atualizar status da inscrição
export interface AtualizarStatusInscricaoDto {
  statusInscricao: StatusInscricao;
}
