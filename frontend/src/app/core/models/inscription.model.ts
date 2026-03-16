import { StatusInscricao, Games } from './enums';

// Domain básico da inscrição
export interface InscricaoDomain {
  id: number;
  torneioId: number;
  equipeId: number;
  statusInscricao: StatusInscricao;
  dataInscricao: string;
}

// DTO detalhado retornado pelo backend (usado em listagens)
export interface InscricaoDetalhadaDto {
  id: number;
  torneioId: number;
  nomeTorneio: string;
  inicioTorneio: string;
  jogoTorneio: Games;
  equipeId: number;
  nomeEquipe: string;
  liderId: number;
  statusInscricao: StatusInscricao;
  motivoRecusa?: string;
  dataInscricao: string;
}

// DTO para criar inscrição
export interface CriarInscricaoDto {
  torneioId: number;
  equipeId: number;
}

// DTO para atualizar status da inscrição (ADMIN/Criador do torneio)
export interface AtualizarStatusInscricaoDto {
  statusInscricao: StatusInscricao;
  motivoRecusa?: string;
}
