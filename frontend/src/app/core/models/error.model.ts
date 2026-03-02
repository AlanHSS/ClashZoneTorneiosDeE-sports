// Modelo de erro padrão retornado pelo backend
export interface BackendError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;

  // Campos específicos de cada exception
  campo?: string;
  valor?: string;
  camposFaltantes?: string[];
  errors?: string[];

  // CampoDuplicadoException
  // campo, valor já cobertos acima

  // InscricaoDuplicadaException
  torneioId?: number;
  equipeId?: number;

  // TorneioSemVagasException
  vagasOcupadas?: number;
  vagasTotais?: number;

  // JogoIncompativelException
  jogoEquipe?: string;
  jogoTorneio?: string;

  // EquipeIncompletaException
  jogadoresAtuais?: number;
  jogadoresMinimos?: number;
  jogo?: string;

  // TorneioNaoDisponivelException
  statusAtual?: string;

  // StatusInscricaoInvalidoException
  novoStatus?: string;
}

// Helper para extrair mensagem de erro
export class ErrorHelper {
  static extractMessage(error: any): string {
    // Caso 1: Objeto de erro padrão do backend
    if (error?.error && typeof error.error === 'object') {
      const backendError = error.error as BackendError;

      // Mensagem base
      let message = backendError.message || 'Erro desconhecido';

      // Adicionar detalhes extras se disponíveis
      if (backendError.camposFaltantes && backendError.camposFaltantes.length > 0) {
        message += `: ${backendError.camposFaltantes.join(', ')}`;
      }

      if (backendError.errors && backendError.errors.length > 0) {
        message = backendError.errors.join('; ');
      }

      return message;
    }

    // Caso 2: String direta
    if (typeof error?.error === 'string') {
      return error.error;
    }

    // Caso 3: Mensagem genérica
    return error?.message || 'Ocorreu um erro inesperado';
  }

  static isNetworkError(error: any): boolean {
    return error.status === 0;
  }

  static isAuthError(error: any): boolean {
    return error.status === 401;
  }

  static isForbiddenError(error: any): boolean {
    return error.status === 403;
  }

  static isNotFoundError(error: any): boolean {
    return error.status === 404;
  }

  static isConflictError(error: any): boolean {
    return error.status === 409;
  }

  static isValidationError(error: any): boolean {
    return error.status === 400 &&
           (error.error?.errors || error.error?.camposFaltantes);
  }

  static isServerError(error: any): boolean {
    return error.status >= 500 && error.status < 600;
  }
}
