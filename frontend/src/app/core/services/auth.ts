import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import {
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  UsuariosDomain
} from '@core/models';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly API_URL = `${environment.apiUrl}/auth`;
  private readonly STORAGE_TOKEN = environment.storageKeys.token;
  private readonly STORAGE_USER = environment.storageKeys.user;

  private currentUserSubject = new BehaviorSubject<UsuariosDomain | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, data).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  logout(): void {
    localStorage.removeItem(this.STORAGE_TOKEN);
    localStorage.removeItem(this.STORAGE_USER);
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.STORAGE_TOKEN);
  }

  getCurrentUser(): UsuariosDomain | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'ADMIN';
  }

  private handleAuthSuccess(response: AuthResponse): void {
    const token = `${response.tipo} ${response.token}`;
    localStorage.setItem(this.STORAGE_TOKEN, token);

    const user: UsuariosDomain = {
      id: response.usuario.id,
      nomeDoUsuario: response.usuario.nome,
      nickname: response.usuario.nickname,
      emailDoUsuario: response.usuario.email,
      dataCriacao: response.usuario.dataCriacao || new Date().toISOString(),
      role: response.usuario.role
    };

    localStorage.setItem(this.STORAGE_USER, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getUserFromStorage(): UsuariosDomain | null {
    const userJson = localStorage.getItem(this.STORAGE_USER);
    return userJson ? JSON.parse(userJson) : null;
  }
}
