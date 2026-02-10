import { Role } from './enums';

export interface UsuariosDomain {
  id: number;
  nomeDoUsuario: string;
  nickname: string;
  emailDoUsuario: string;
  dataCriacao: string;
  role: Role;
}

export interface LoginRequest {
  emailDoUsuario: string;
  senhaDoUsuario: string;
}

export interface RegisterRequest {
  nomeDoUsuário: string;
  nickname: string;
  emailDoUsuario: string;
  senhaDoUsuario: string;
}

export interface AuthResponse {
  token: string;
  tipo: string;
  usuario: {
    id: number;
    nome: string;
    nickname: string;
    email: string;
    dataCriacao?: string;
    role: Role;
  };
}

export interface AtualizarUsuariosDto {
  nomeDoUsuario?: string;
  emailDoUsuario?: string;
  senhaDoUsuario?: string;
}
