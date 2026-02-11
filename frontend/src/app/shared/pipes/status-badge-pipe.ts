import { Pipe, PipeTransform } from '@angular/core';
import { StatusDoTorneio, StatusInscricao } from '@core/models';

@Pipe({
  name: 'statusBadge',
  standalone: true
})
export class StatusBadgePipe implements PipeTransform {
  transform(value: StatusDoTorneio | StatusInscricao): string {
    const statusClasses: Record<string, string> = {
      // Torneio
      'AGENDADO': 'status-agendado',
      'EM_ANDAMENTO': 'status-em-andamento',
      'FINALIZADO': 'status-finalizado',
      'CANCELADO': 'status-cancelado',

      // Inscrição
      'PENDENTE': 'status-pendente',
      'APROVADA': 'status-aprovada',
      'RECUSADA': 'status-recusada',
      'CANCELADA': 'status-cancelada'
    };

    return statusClasses[value] || '';
  }
}
