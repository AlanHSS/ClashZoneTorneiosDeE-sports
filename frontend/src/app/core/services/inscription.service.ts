import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  InscricaoDomain,
  CriarInscricaoDto,
  AtualizarStatusInscricaoDto
} from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/inscricao`;

  criar(inscricao: CriarInscricaoDto): Observable<InscricaoDomain> {
    return this.http.post<InscricaoDomain>(this.API_URL, inscricao);
  }

  listarPorTorneio(torneioId: number): Observable<InscricaoDomain[]> {
    return this.http.get<InscricaoDomain[]>(`${this.API_URL}/torneio/${torneioId}`);
  }

  listarPorEquipe(equipeId: number): Observable<InscricaoDomain[]> {
    return this.http.get<InscricaoDomain[]>(`${this.API_URL}/equipe/${equipeId}`);
  }

  atualizarStatus(id: number, status: AtualizarStatusInscricaoDto): Observable<InscricaoDomain> {
    return this.http.patch<InscricaoDomain>(`${this.API_URL}/${id}/status`, status);
  }

  listarMinhasInscricoes(): Observable<InscricaoDomain[]> {
    return this.http.get<InscricaoDomain[]>(`${this.API_URL}/minhas`);
  }
}
