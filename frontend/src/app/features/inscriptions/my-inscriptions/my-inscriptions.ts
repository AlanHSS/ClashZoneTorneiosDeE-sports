import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { InscriptionService } from '@core/services/inscription';
import { NotificationService } from '@core/services/notification';
import { InscricaoDetalhadaDto, StatusInscricao } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';

@Component({
  selector: 'app-my-inscriptions',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    GameNamePipe,
    StatusNamePipe,
    StatusBadgePipe
  ],
  templateUrl: './my-inscriptions.html',
  styleUrl: './my-inscriptions.scss'
})
export class MyInscriptionsComponent implements OnInit {
  private inscriptionService = inject(InscriptionService);
  private notificationService = inject(NotificationService);

  inscriptions: InscricaoDetalhadaDto[] = [];
  filteredInscriptions: InscricaoDetalhadaDto[] = [];
  loading = true;

  statusList = Object.values(StatusInscricao);

  filters = {
    status: null as StatusInscricao | null
  };

  ngOnInit(): void {
    this.loadInscriptions();
  }

  loadInscriptions(): void {
    this.loading = true;

    this.inscriptionService.listarMinhasInscricoes().subscribe({
      next: (response) => {
        // Backend retorna { "Minhas Inscrições": [...] }
        this.inscriptions = response['Minhas Inscrições'] || [];
        this.filteredInscriptions = this.inscriptions;
        this.loading = false;
      },
      error: () => {
        this.inscriptions = [];
        this.filteredInscriptions = [];
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.inscriptions];

    if (this.filters.status) {
      filtered = filtered.filter(i => i.statusInscricao === this.filters.status);
    }

    this.filteredInscriptions = filtered;
  }

  clearFilters(): void {
    this.filters = {
      status: null
    };
    this.applyFilters();
  }

  hasActiveFilters(): boolean {
    return this.filters.status !== null;
  }

  canCancel(inscription: InscricaoDetalhadaDto): boolean {
    // Só pode cancelar se estiver PENDENTE
    return inscription.statusInscricao === StatusInscricao.PENDENTE;
  }

  onCancel(inscription: InscricaoDetalhadaDto): void {
    if (confirm(`Tem certeza que deseja cancelar a inscrição da equipe "${inscription.nomeEquipe}" no torneio "${inscription.nomeTorneio}"?`)) {
      this.inscriptionService.atualizarStatus(inscription.id, {
        statusInscricao: StatusInscricao.CANCELADA
      }).subscribe({
        next: () => {
          this.notificationService.success('Inscrição cancelada com sucesso!');
          this.loadInscriptions();
        }
      });
    }
  }
}
