import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  EquipeDomain,
  CriarEquipeDto,
  AtualizarEquipeDto,
  MembroEquipeDomain,
  AdicionarMembroDto,
  AtualizarMembroDto
} from '@core/models';
import { environment } from '@environments/environment';

type AnyRecord = Record<string, any>;

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/equipes`;

  private extractEquipeFromResponse(response: any): EquipeDomain {

    if (!response) return response as EquipeDomain;

    if (response.equipe) return response.equipe as EquipeDomain;
    if (response['Dados da equipe: ']) return response['Dados da equipe: '] as EquipeDomain;
    if (response['Dados da equipe']) return response['Dados da equipe'] as EquipeDomain;

    const suasEquipes = (response as AnyRecord)['Suas equipes'];
    if (suasEquipes) {
      if (Array.isArray(suasEquipes) && (suasEquipes[0] as AnyRecord)?.['equipe']) {
        return (suasEquipes[0] as AnyRecord)['equipe'] as EquipeDomain;
      }
      if (typeof suasEquipes === 'object' && (suasEquipes as AnyRecord)['equipe']) {
        return (suasEquipes as AnyRecord)['equipe'] as EquipeDomain;
      }
    }

    return response as EquipeDomain;
  }

  private extractEquipesFromResponse(response: any): EquipeDomain[] {

    if (!response) return [];
    if (Array.isArray(response)) return response as EquipeDomain[];

    const suasEquipes = (response as AnyRecord)['Suas equipes'];
    if (Array.isArray(suasEquipes)) {
      return suasEquipes
        .map((item) => ((item as AnyRecord)?.['equipe'] ?? item) as EquipeDomain)
        .filter(Boolean);
    }

    if (typeof suasEquipes === 'object' && (suasEquipes as AnyRecord)['equipe']) {
      return [((suasEquipes as AnyRecord)['equipe'] as EquipeDomain)].filter(Boolean);
    }

    return [];
  }

  // GET /clashzone/equipes/listartodasequipes (ADMIN only)
  listarEquipes(): Observable<EquipeDomain[]> {
    return this.http.get<EquipeDomain[]>(`${this.API_URL}/listartodasequipes`);
  }

  // GET /clashzone/equipes/informacoesdaequipe/{id}
  buscarPorId(id: number): Observable<EquipeDomain> {
    return this.http
      .get<any>(`${this.API_URL}/informacoesdaequipe/${id}`)
      .pipe(map((response) => this.extractEquipeFromResponse(response)));
  }

  // POST /clashzone/equipes/criarequipe
  criar(equipe: CriarEquipeDto): Observable<EquipeDomain> {
    return this.http.post<any>(`${this.API_URL}/criarequipe`, equipe).pipe(
      map(response => response['Dados da equipe: '] || response)
    );
  }

  // PATCH /clashzone/equipes/atualizarequipe/{id}
  atualizar(id: number, equipe: AtualizarEquipeDto): Observable<EquipeDomain> {
    return this.http.patch<any>(`${this.API_URL}/atualizarequipe/${id}`, equipe).pipe(
      map(response => response['Dados da equipe'] || response)
    );
  }

  // DELETE /clashzone/equipes/deletarequipe/{id}
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deletarequipe/${id}`);
  }

  // GET /clashzone/equipes/minhasequipes
  listarMinhasEquipes(): Observable<EquipeDomain[]> {
    return this.http
      .get<any>(`${this.API_URL}/minhasequipes`)
      .pipe(map((response) => this.extractEquipesFromResponse(response)));
  }

  // ==========================================
  // GERENCIAMENTO DE MEMBROS
  // ==========================================

  // GET /clashzone/equipes/{equipeId}/membros/listarmembros (ADMIN only)
  listarMembros(equipeId: number): Observable<MembroEquipeDomain[]> {
    return this.http.get<MembroEquipeDomain[]>(`${this.API_URL}/${equipeId}/membros/listarmembros`);
  }

  // POST /clashzone/equipes/{equipeId}/membros/adicionar
  adicionarMembro(equipeId: number, membro: AdicionarMembroDto): Observable<any> {
    // Backend espera array de membros
    const membros = [membro];
    return this.http.post<any>(`${this.API_URL}/${equipeId}/membros/adicionar`, membros);
  }

  // PATCH /clashzone/equipes/{equipeId}/membros/atualizar
  atualizarMembro(equipeId: number, membro: AtualizarMembroDto): Observable<any> {
    // Backend espera array de membros
    const membros = [membro];
    return this.http.patch<any>(`${this.API_URL}/${equipeId}/membros/atualizar`, membros);
  }

  // DELETE /clashzone/equipes/{equipeId}/membros/deletar/{membroId}
  removerMembro(equipeId: number, membroId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${equipeId}/membros/deletar/${membroId}`);
  }
}
