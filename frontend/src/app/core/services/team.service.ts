import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  EquipeDomain,
  CriarEquipeDto,
  AtualizarEquipeDto,
  MembroEquipeDomain,
  AdicionarMembroDto,
  AtualizarMembroDto
} from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/equipes`;

  listarEquipes(): Observable<EquipeDomain[]> {
    return this.http.get<EquipeDomain[]>(this.API_URL);
  }

  buscarPorId(id: number): Observable<EquipeDomain> {
    return this.http.get<EquipeDomain>(`${this.API_URL}/${id}`);
  }

  criar(equipe: CriarEquipeDto): Observable<EquipeDomain> {
    return this.http.post<EquipeDomain>(this.API_URL, equipe);
  }

  atualizar(id: number, equipe: AtualizarEquipeDto): Observable<EquipeDomain> {
    return this.http.patch<EquipeDomain>(`${this.API_URL}/${id}`, equipe);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  listarMinhasEquipes(): Observable<EquipeDomain[]> {
    return this.http.get<EquipeDomain[]>(`${this.API_URL}/minhas`);
  }

  // Gerenciamento de membros
  listarMembros(equipeId: number): Observable<MembroEquipeDomain[]> {
    return this.http.get<MembroEquipeDomain[]>(`${this.API_URL}/${equipeId}/membros`);
  }

  adicionarMembro(equipeId: number, membro: AdicionarMembroDto): Observable<MembroEquipeDomain> {
    return this.http.post<MembroEquipeDomain>(`${this.API_URL}/${equipeId}/membros`, membro);
  }

  atualizarMembro(equipeId: number, membro: AtualizarMembroDto): Observable<MembroEquipeDomain> {
    return this.http.patch<MembroEquipeDomain>(`${this.API_URL}/${equipeId}/membros`, membro);
  }

  removerMembro(equipeId: number, membroId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${equipeId}/membros/${membroId}`);
  }
}
