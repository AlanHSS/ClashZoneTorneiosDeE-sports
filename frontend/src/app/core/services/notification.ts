import { Injectable, inject } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private snackBar = inject(MatSnackBar);

  private readonly defaultConfig: MatSnackBarConfig = {
    horizontalPosition: 'end',
    verticalPosition: 'top',
  };

  success(message: string, duration: number = 3000): void {
    this.snackBar.open(message, 'Fechar', {
      ...this.defaultConfig,
      duration,
      panelClass: ['snackbar-success']
    });
  }

  error(message: string, duration: number = 5000): void {
    this.snackBar.open(message, 'Fechar', {
      ...this.defaultConfig,
      duration,
      panelClass: ['snackbar-error']
    });
  }

  warning(message: string, duration: number = 4000): void {
    this.snackBar.open(message, 'Fechar', {
      ...this.defaultConfig,
      duration,
      panelClass: ['snackbar-warning']
    });
  }

  info(message: string, duration: number = 3000): void {
    this.snackBar.open(message, 'Fechar', {
      ...this.defaultConfig,
      duration,
      panelClass: ['snackbar-info']
    });
  }

  // Métodos específicos para tipos de erro
  conflict(message: string): void {
    this.warning(message, 5000);
  }

  notFound(message: string): void {
    this.error(message, 4000);
  }

  unauthorized(message: string): void {
    this.error(message, 4000);
  }

  forbidden(message: string): void {
    this.error(message, 4000);
  }

  validation(message: string): void {
    this.warning(message, 5000);
  }

  serverError(message: string): void {
    this.error(message, 6000);
  }

  networkError(): void {
    this.error('Não foi possível conectar ao servidor. Verifique sua conexão.', 6000);
  }
}
