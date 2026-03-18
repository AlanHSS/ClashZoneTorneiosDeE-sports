import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, shareReplay } from 'rxjs';
import { UsuariosDomain, AtualizarUsuariosDto, PublicUsuarioDto } from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/usuarios`;
  private publicUserCache = new Map<number, Observable<PublicUsuarioDto>>();

  // GET /clashzone/usuarios/userprofile/{id}
  buscarPerfil(): Observable<UsuariosDomain> {
    // Precisa pegar o ID do usuário logado
    // Por enquanto, deixo preparado para receber o ID
    throw new Error('Use buscarPorId(id) ao invés de buscarPerfil()');
  }

  // PATCH /clashzone/usuarios/atualizarusuario/{id}
  atualizarPerfil(id: number, dados: AtualizarUsuariosDto): Observable<UsuariosDomain> {
    return this.http.patch<any>(`${this.API_URL}/atualizarusuario/${id}`, dados).pipe(
      map(response => response['Dados do usuário: '] || response)
    );
  }

  // DELETE /clashzone/usuarios/deletarusuario/{id}
  deletarConta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deletarusuario/${id}`);
  }

  // GET /clashzone/usuarios/listartodosusuarios (ADMIN only)
  listarTodos(): Observable<UsuariosDomain[]> {
    return this.http.get<UsuariosDomain[]>(`${this.API_URL}/listartodosusuarios`);
  }

  // GET /clashzone/usuarios/userprofile/{id}
  buscarPorId(id: number): Observable<UsuariosDomain> {
    return this.http.get<UsuariosDomain>(`${this.API_URL}/userprofile/${id}`);
  }

  // GET /clashzone/usuarios/public/{id}
  buscarPublicoPorId(id: number): Observable<PublicUsuarioDto> {
    const cached = this.publicUserCache.get(id);
    if (cached) return cached;

    const request$ = this.http
      .get<PublicUsuarioDto>(`${this.API_URL}/public/${id}`)
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));

    this.publicUserCache.set(id, request$);
    return request$;
  }
}
