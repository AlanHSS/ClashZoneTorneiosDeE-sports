import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { ToastService } from '@core/services/toast';
import { AuthService } from '@core/services/auth';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const toastService = inject(ToastService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('❌ HTTP Error capturado:', error);
      console.error('📦 Error completo:', JSON.stringify(error.error, null, 2));

      let errorMessage = 'Ocorreu um erro inesperado';
      let shouldLogout = false;
      let shouldRedirect = false;

      // ==========================================
      // EXTRAIR MENSAGEM DO BACKEND
      // ==========================================
      if (error.error && typeof error.error === 'object') {
        // Formato padrão do GlobalExceptionHandler:
        // { timestamp, status, error, message, path }
        if (error.error.message) {
          errorMessage = error.error.message;
        }

        // Exibir detalhes adicionais se disponíveis
        if (error.error.campo && error.error.valor) {
          // CampoDuplicadoException
          errorMessage = error.error.message;
        }

        if (error.error.camposFaltantes) {
          // CampoObrigatorioException
          const campos = error.error.camposFaltantes.join(', ');
          errorMessage = `${error.error.message}: ${campos}`;
        }

        if (error.error.errors && Array.isArray(error.error.errors)) {
          // MethodArgumentNotValidException
          errorMessage = error.error.errors.join('; ');
        }
      }
      // Fallback: se error.error for string
      else if (typeof error.error === 'string') {
        errorMessage = error.error;
      }

      // ==========================================
      // TRATAMENTO POR STATUS CODE
      // ==========================================
      switch (error.status) {
        case 0:
          // Erro de rede / CORS / Backend offline
          errorMessage = 'Não foi possível conectar ao servidor. Verifique sua conexão ou se o backend está rodando.';
          toastService.error(errorMessage, 6000);
          break;

        case 400:
          // Bad Request - Validação, dados inválidos
          toastService.error(errorMessage, 5000);
          break;

        case 401:
          // Unauthorized - Token inválido/expirado
          errorMessage = error.error?.message || 'Sessão expirada. Faça login novamente.';
          toastService.error(errorMessage);
          shouldLogout = true;
          break;

        case 403:
          // Forbidden - Sem permissão
          errorMessage = error.error?.message || 'Você não tem permissão para acessar este recurso.';
          toastService.error(errorMessage);
          shouldRedirect = true;
          break;

        case 404:
          // Not Found
          toastService.error(errorMessage);
          break;

        case 409:
          // Conflict - Duplicação (CampoDuplicadoException, InscricaoDuplicadaException)
          toastService.warning(errorMessage, 5000);
          break;

        case 500:
          // Internal Server Error
          console.error('🔥 Erro 500 do backend:', error);
          errorMessage = error.error?.message || 'Erro interno do servidor. Tente novamente mais tarde.';
          toastService.error(errorMessage, 6000);
          break;

        case 503:
          // Service Unavailable
          errorMessage = 'Serviço temporariamente indisponível. Tente novamente em alguns instantes.';
          toastService.error(errorMessage);
          break;

        default:
          // Outros erros
          toastService.error(errorMessage);
          break;
      }

      // ==========================================
      // AÇÕES PÓS-ERRO
      // ==========================================
      if (shouldLogout) {
        setTimeout(() => authService.logout(), 2000);
      }

      if (shouldRedirect) {
        setTimeout(() => router.navigate(['/tournaments']), 2000);
      }

      // ==========================================
      // LOG DETALHADO PARA DEBUG
      // ==========================================
      console.error('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
      console.error('❌ ERRO HTTP INTERCEPTADO');
      console.error('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
      console.error('📍 URL:', req.url);
      console.error('🔢 Status:', error.status);
      console.error('⚠️  Error Type:', error.error?.error || 'Unknown');
      console.error('💬 Message:', errorMessage);
      if (error.error?.campo) {
        console.error('📝 Campo:', error.error.campo);
      }
      if (error.error?.valor) {
        console.error('📊 Valor:', error.error.valor);
      }
      if (error.error?.path) {
        console.error('🛤️  Path:', error.error.path);
      }
      console.error('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');

      return throwError(() => error);
    })
  );
};
