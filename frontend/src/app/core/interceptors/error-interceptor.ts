import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const notificationService = inject(NotificationService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocorreu um erro inesperado';

      if (error.error instanceof ErrorEvent) {
        errorMessage = `Erro: ${error.error.message}`;
      } else {
        switch (error.status) {
          case 400:
            errorMessage = error.error?.message || 'Requisição inválida';
            break;
          case 401:
            errorMessage = 'Sessão expirada. Faça login novamente';
            authService.logout();
            break;
          case 403:
            errorMessage = 'Você não tem permissão para acessar este recurso';
            router.navigate(['/tournaments']);
            break;
          case 404:
            errorMessage = error.error?.message || 'Recurso não encontrado';
            break;
          case 409:
            errorMessage = error.error?.message || 'Conflito de dados';
            break;
          case 500:
            errorMessage = 'Erro interno do servidor. Tente novamente mais tarde';
            break;
          case 503:
            errorMessage = 'Serviço temporariamente indisponível';
            break;
          default:
            errorMessage = error.error?.message || `Erro ${error.status}: ${error.statusText}`;
        }
      }

      notificationService.error(errorMessage);
      return throwError(() => error);
    })
  );
};
