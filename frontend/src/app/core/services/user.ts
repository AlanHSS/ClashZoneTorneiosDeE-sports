import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuariosDomain, AtualizarUsuariosDto } from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/usuarios`;

  buscarPerfil(): Observable<UsuariosDomain> {
    return this.http.get<UsuariosDomain>(`${this.API_URL}/perfil`);
  }

  atualizarPerfil(dados: AtualizarUsuariosDto): Observable<UsuariosDomain> {
    return this.http.patch<UsuariosDomain>(`${this.API_URL}/perfil`, dados);
  }

  deletarConta(): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/perfil`);
  }

  listarTodos(): Observable<UsuariosDomain[]> {
    return this.http.get<UsuariosDomain[]>(this.API_URL);
  }

  buscarPorId(id: number): Observable<UsuariosDomain> {
    return this.http.get<UsuariosDomain>(`${this.API_URL}/${id}`);
  }
}
