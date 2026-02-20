import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { TournamentService } from '@core/services/tournament';
import { InscriptionService } from '@core/services/inscription';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';
import { TorneioDomain, InscricaoDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';

@Component({
  selector: 'app-tournament-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    GameNamePipe,
    StatusBadgePipe
  ],
  templateUrl: './tournament-detail.html',
  styleUrl: './tournament-detail.scss'
})
export class TournamentDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private tournamentService = inject(TournamentService);
  private inscriptionService = inject(InscriptionService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);

  tournament: TorneioDomain | null = null;
  inscriptions: InscricaoDomain[] = [];
  loading = true;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadTournament(id);
      this.loadInscriptions(id);
    }
  }

  loadTournament(id: number): void {
    this.tournamentService.buscarPorId(id).subscribe({
      next: (data) => {
        this.tournament = data;
        this.loading = false;
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneio');
        this.router.navigate(['/tournaments']);
      }
    });
  }

  loadInscriptions(id: number): void {
    this.inscriptionService.listarPorTorneio(id).subscribe({
      next: (data) => {
        this.inscriptions = data;
      },
      error: () => {
        // Não exibe erro se não conseguir carregar inscrições
      }
    });
  }

  onEdit(): void {
    if (this.tournament) {
      this.router.navigate(['/tournaments/edit', this.tournament.id]);
    }
  }

  onDelete(): void {
    if (!this.tournament) return;

    if (confirm(`Tem certeza que deseja excluir o torneio "${this.tournament.nomeDoTorneio}"?`)) {
      this.tournamentService.deletar(this.tournament.id).subscribe({
        next: () => {
          this.notificationService.success('Torneio excluído com sucesso!');
          this.router.navigate(['/tournaments']);
        },
        error: () => {
          this.notificationService.error('Erro ao excluir torneio');
        }
      });
    }
  }

  onRegister(): void {
    if (this.tournament) {
      this.router.navigate(['/inscriptions/create'], {
        queryParams: { tournamentId: this.tournament.id }
      });
    }
  }

  onBack(): void {
    this.router.navigate(['/tournaments']);
  }

  canEdit(): boolean {
    if (!this.tournament) return false;
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.id === this.tournament.criadorId || this.authService.isAdmin();
  }

  canRegister(): boolean {
    return this.tournament?.statusDoTorneio === 'AGENDADO';
  }

  getInscriptionsCount(): number {
    return this.inscriptions.filter(i => i.statusInscricao === 'APROVADA').length;
  }

  getAvailableSlots(): number {
    if (!this.tournament) return 0;
    return this.tournament.quantidadeDeEquipes - this.getInscriptionsCount();
  }
}
