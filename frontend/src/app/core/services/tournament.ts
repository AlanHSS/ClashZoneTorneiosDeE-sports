import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  TorneioDomain,
  CriarTorneioDto,
  AtualizarTorneioDto,
  FiltroTorneioDto
} from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/torneios`;

  // GET /clashzone/torneios/listartorneios
  listarTorneios(filtros?: FiltroTorneioDto): Observable<TorneioDomain[]> {
    // Se houver filtros, usar endpoint de filtros
    if (filtros && this.hasFiltros(filtros)) {
      return this.listarTorneiosFiltrados(filtros);
    }

    // Sem filtros, usar endpoint simples
    return this.http.get<TorneioDomain[]>(`${this.API_URL}/listartorneios`);
  }

  // GET /clashzone/torneios/listartorneios/paginado?page=0&size=10&sort=dataCriacao,desc
  listarTorneiosPaginado(params?: {
    page?: number;
    size?: number;
    sort?: string[];
  }): Observable<{
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    content: TorneioDomain[];
  }> {
    let httpParams = new HttpParams();
    if (params?.page != null) httpParams = httpParams.set('page', params.page);
    if (params?.size != null) httpParams = httpParams.set('size', params.size);
    if (params?.sort?.length) {
      for (const s of params.sort) httpParams = httpParams.append('sort', s);
    }

    return this.http.get<any>(`${this.API_URL}/listartorneios/paginado`, { params: httpParams });
  }

  // POST /clashzone/torneios/torneiosfiltrados
  listarTorneiosFiltrados(filtros: FiltroTorneioDto): Observable<TorneioDomain[]> {
    return this.http.post<any>(`${this.API_URL}/torneiosfiltrados`, filtros).pipe(
      map(response => response['Lista de torneios'] || [])
    );
  }

  // POST /clashzone/torneios/torneiosfiltrados/paginado?page=0&size=10&sort=dataCriacao,desc
  listarTorneiosFiltradosPaginado(
    filtros: FiltroTorneioDto,
    params?: {
      page?: number;
      size?: number;
      sort?: string[];
    }
  ): Observable<{
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    content: TorneioDomain[];
  }> {
    let httpParams = new HttpParams();
    if (params?.page != null) httpParams = httpParams.set('page', params.page);
    if (params?.size != null) httpParams = httpParams.set('size', params.size);
    if (params?.sort?.length) {
      for (const s of params.sort) httpParams = httpParams.append('sort', s);
    }

    return this.http.post<any>(`${this.API_URL}/torneiosfiltrados/paginado`, filtros, { params: httpParams });
  }

  // GET /clashzone/torneios/paginadotorneio/{id}
  buscarPorId(id: number): Observable<TorneioDomain> {
    return this.http.get<TorneioDomain>(`${this.API_URL}/paginadotorneio/${id}`);
  }

  // POST /clashzone/torneios/criartorneio
  criar(torneio: CriarTorneioDto): Observable<TorneioDomain> {
    console.log('🚀 TournamentService.criar() chamado');
    console.log('📤 Dados enviados:', torneio);
    console.log('🌐 URL:', `${this.API_URL}/criartorneio`);

    return this.http.post<any>(`${this.API_URL}/criartorneio`, torneio).pipe(
      map(response => {
        console.log('✅ Response recebida:', response);
        return response['Dados do torneio: '] || response;
      })
    );
  }

  // PATCH /clashzone/torneios/atualizartorneio/{id}
  atualizar(id: number, torneio: AtualizarTorneioDto): Observable<TorneioDomain> {
    return this.http.patch<any>(`${this.API_URL}/atualizartorneio/${id}`, torneio).pipe(
      map(response => response['Dados do torneio: '] || response)
    );
  }

  // Não existe endpoint de deletar no backend
  deletar(id: number): Observable<void> {
    // Backend não tem este endpoint ainda
    throw new Error('Endpoint de deletar torneio não implementado no backend');
  }

  // GET /clashzone/torneios/meustorneios
  listarMeusTorneios(): Observable<TorneioDomain[]> {
    return this.http.get<TorneioDomain[]>(`${this.API_URL}/meustorneios`);
  }

  // Alias para compatibilidade
  listarTorneiosDisponiveis(): Observable<TorneioDomain[]> {
    return this.listarTorneios();
  }

  // Método auxiliar para verificar se há filtros
  private hasFiltros(filtros: FiltroTorneioDto): boolean {
    return !!(
      filtros.nomeDoTorneio ||
      filtros.jogoDoTorneio ||
      filtros.statusDoTorneio ||
      filtros.plataforma
    );
  }
}
