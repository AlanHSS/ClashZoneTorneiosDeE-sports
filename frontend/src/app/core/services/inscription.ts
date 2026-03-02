import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  InscricaoDomain,
  CriarInscricaoDto,
  AtualizarStatusInscricaoDto,
  StatusInscricao
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

  // GET /clashzone/inscricao/equipe/{equipeId}?status=APROVADA
  listarPorEquipe(equipeId: number, status?: StatusInscricao): Observable<any> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<any>(`${this.API_URL}/equipe/${equipeId}`, { params });
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
}
