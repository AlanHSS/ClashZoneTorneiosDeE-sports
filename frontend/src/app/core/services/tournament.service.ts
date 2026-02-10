import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
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

  listarTorneios(filtros?: FiltroTorneioDto): Observable<TorneioDomain[]> {
    let params = new HttpParams();

    if (filtros) {
      if (filtros.nomeDoTorneio) params = params.set('nomeDoTorneio', filtros.nomeDoTorneio);
      if (filtros.inicioDoTorneio) params = params.set('inicioDoTorneio', filtros.inicioDoTorneio);
      if (filtros.jogoDoTorneio) params = params.set('jogoDoTorneio', filtros.jogoDoTorneio);
      if (filtros.statusDoTorneio) params = params.set('statusDoTorneio', filtros.statusDoTorneio);
      if (filtros.plataforma) params = params.set('plataforma', filtros.plataforma);
    }

    return this.http.get<TorneioDomain[]>(this.API_URL, { params });
  }

  buscarPorId(id: number): Observable<TorneioDomain> {
    return this.http.get<TorneioDomain>(`${this.API_URL}/${id}`);
  }

  criar(torneio: CriarTorneioDto): Observable<TorneioDomain> {
    return this.http.post<TorneioDomain>(this.API_URL, torneio);
  }

  atualizar(id: number, torneio: AtualizarTorneioDto): Observable<TorneioDomain> {
    return this.http.patch<TorneioDomain>(`${this.API_URL}/${id}`, torneio);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  listarMeusTorneios(): Observable<TorneioDomain[]> {
    return this.http.get<TorneioDomain[]>(`${this.API_URL}/meus`);
  }

  listarTorneiosDisponiveis(): Observable<TorneioDomain[]> {
    return this.http.get<TorneioDomain[]>(`${this.API_URL}/disponiveis`);
  }
}
