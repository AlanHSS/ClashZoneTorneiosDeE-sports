import { Pipe, PipeTransform } from '@angular/core';
import { StatusDoTorneio, StatusInscricao } from '@core/models';

@Pipe({
  name: 'statusName',
  standalone: true
})
export class StatusNamePipe implements PipeTransform {
  private statusNames: Record<string, string> = {
    // Status de Torneio
    'AGENDADO': 'Agendado',
    'EM_ANDAMENTO': 'Em Andamento',
    'FINALIZADO': 'Finalizado',
    'CANCELADO': 'Cancelado',

    // Status de Inscrição
    'PENDENTE': 'Pendente',
    'APROVADA': 'Aprovada',
    'RECUSADA': 'Recusada',
    'CANCELADA': 'Cancelada'
  };

  transform(value: StatusDoTorneio | StatusInscricao | string): string {
    return this.statusNames[value] || value;
  }
}
