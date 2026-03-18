import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  InscricaoDomain,
  CriarInscricaoDto,
  AtualizarStatusInscricaoDto,
  StatusInscricao,
  MembroEquipeDomain
} from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/inscricao`;

  // POST /clashzone/inscricao/criar
  criar(inscricao: CriarInscricaoDto): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/criar`, inscricao);
  }

  // GET /clashzone/inscricao/torneio/{torneioId}?status=PENDENTE
  listarPorTorneio(torneioId: number, status?: StatusInscricao): Observable<any> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<any>(`${this.API_URL}/torneio/${torneioId}`, { params });
  }

  // GET /clashzone/inscricao/torneio/{torneioId}/paginado?page=0&size=10&sort=dataInscricao,desc
  listarPorTorneioPaginado(
    torneioId: number,
    params?: {
      status?: StatusInscricao;
      page?: number;
      size?: number;
      sort?: string[];
    }
  ): Observable<any> {
    let httpParams = new HttpParams();
    if (params?.status) httpParams = httpParams.set('status', params.status);
    if (params?.page != null) httpParams = httpParams.set('page', params.page);
    if (params?.size != null) httpParams = httpParams.set('size', params.size);
    if (params?.sort?.length) {
      for (const s of params.sort) httpParams = httpParams.append('sort', s);
    }

    return this.http.get<any>(`${this.API_URL}/torneio/${torneioId}/paginado`, { params: httpParams });
  }

  // GET /clashzone/inscricao/equipe/{equipeId}?status=APROVADA
  listarPorEquipe(equipeId: number, status?: StatusInscricao): Observable<any> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<any>(`${this.API_URL}/equipe/${equipeId}`, { params });
  }

  // GET /clashzone/inscricao/equipe/{equipeId}/paginado?page=0&size=10&sort=dataInscricao,desc
  listarPorEquipePaginado(
    equipeId: number,
    params?: {
      status?: StatusInscricao;
      page?: number;
      size?: number;
      sort?: string[];
    }
  ): Observable<any> {
    let httpParams = new HttpParams();
    if (params?.status) httpParams = httpParams.set('status', params.status);
    if (params?.page != null) httpParams = httpParams.set('page', params.page);
    if (params?.size != null) httpParams = httpParams.set('size', params.size);
    if (params?.sort?.length) {
      for (const s of params.sort) httpParams = httpParams.append('sort', s);
    }

    return this.http.get<any>(`${this.API_URL}/equipe/${equipeId}/paginado`, { params: httpParams });
  }

  // PATCH /clashzone/inscricao/atualizar/{inscricaoId}
  atualizarStatus(id: number, status: AtualizarStatusInscricaoDto): Observable<any> {
    return this.http.patch<any>(`${this.API_URL}/atualizar/${id}`, status);
  }

  // GET /clashzone/inscricao/minhasinscricoes?status=PENDENTE
  listarMinhasInscricoes(status?: StatusInscricao): Observable<any> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<any>(`${this.API_URL}/minhasinscricoes`, { params });
  }

  // GET /clashzone/inscricao/minhasinscricoes/paginado?page=0&size=10&sort=dataInscricao,desc
  listarMinhasInscricoesPaginado(params?: {
    status?: StatusInscricao;
    page?: number;
    size?: number;
    sort?: string[];
  }): Observable<any> {
    let httpParams = new HttpParams();
    if (params?.status) httpParams = httpParams.set('status', params.status);
    if (params?.page != null) httpParams = httpParams.set('page', params.page);
    if (params?.size != null) httpParams = httpParams.set('size', params.size);
    if (params?.sort?.length) {
      for (const s of params.sort) httpParams = httpParams.append('sort', s);
    }

    return this.http.get<any>(`${this.API_URL}/minhasinscricoes/paginado`, { params: httpParams });
  }

  // GET /clashzone/inscricao/torneio/{torneioId}/equipe/{equipeId}/membros
  listarMembrosEquipeNoTorneio(torneioId: number, equipeId: number): Observable<MembroEquipeDomain[]> {
    return this.http.get<any>(`${this.API_URL}/torneio/${torneioId}/equipe/${equipeId}/membros`).pipe(
      map((response) => response?.['Membros'] || response?.Membros || response || [])
    );
  }
}
