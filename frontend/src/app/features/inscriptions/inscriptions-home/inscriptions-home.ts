import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { TournamentService } from '@core/services/tournament';
import { InscriptionService } from '@core/services/inscription';
import { ToastService } from '@core/services/toast';
import { UserService } from '@core/services/user';
import { InscricaoDetalhadaDto, StatusDoTorneio, StatusInscricao, TorneioDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';
import { PlatformNamePipe } from '@shared/pipes/platform-name-pipe';

type TournamentPending = {
  tournament: TorneioDomain;
  pending: InscricaoDetalhadaDto[];
};

@Component({
  selector: 'app-inscriptions-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    GameNamePipe,
    StatusBadgePipe,
    StatusNamePipe,
    PlatformNamePipe
  ],
  templateUrl: './inscriptions-home.html',
  styleUrl: './inscriptions-home.scss'
})
export class InscriptionsHomeComponent implements OnInit {
  private router = inject(Router);
  private tournamentService = inject(TournamentService);
  private inscriptionService = inject(InscriptionService);
  private toastService = inject(ToastService);
  private userService = inject(UserService);

  loading = true;
  scheduledTournaments: TorneioDomain[] = [];
  pendingByTournament: TournamentPending[] = [];
  leaderNameById: Record<number, string> = {};

  ngOnInit(): void {
    this.loadOrganizerPending();
  }

  goToMyInscriptions(): void {
    this.router.navigate(['/inscriptions/my']);
  }

  goToTournamentInscriptions(tournamentId: number): void {
    this.router.navigate(['/inscriptions/tournament', tournamentId]);
  }

  private loadOrganizerPending(): void {
    this.loading = true;

    this.tournamentService.listarMeusTorneios().pipe(
      map((tournaments) =>
        tournaments.filter((t) => t.statusDoTorneio === StatusDoTorneio.AGENDADO)
      ),
      switchMap((scheduled) => {
        this.scheduledTournaments = scheduled;

        if (scheduled.length === 0) {
          return of([] as TournamentPending[]);
        }

        const requests = scheduled.map((tournament) =>
          this.inscriptionService
            .listarPorTorneio(tournament.id, StatusInscricao.PENDENTE)
            .pipe(
              map((response) => ({
                tournament,
                pending: this.extractInscriptions(response)
              })),
              // Se o backend retornar 404 quando nao ha pendentes, tratamos como lista vazia.
              catchError(() => of({ tournament, pending: [] as InscricaoDetalhadaDto[] }))
            )
        );

        return forkJoin(requests);
      }),
      catchError(() => {
        this.toastService.error('Erro ao carregar inscrições pendentes dos seus torneios.');
        return of([] as TournamentPending[]);
      })
    ).subscribe({
      next: (result) => {
        this.pendingByTournament = result.filter((r) => r.pending.length > 0);
        this.loadLeadersPublic(this.pendingByTournament);
        this.loading = false;
      },
      error: () => {
        this.pendingByTournament = [];
        this.loading = false;
      }
    });
  }

  private extractInscriptions(response: any): InscricaoDetalhadaDto[] {
    if (!response) return [];
    if (Array.isArray(response)) return response as InscricaoDetalhadaDto[];

    const possibleKeys = [
      'Inscrições',
      'Inscricoes',
      'Histórico de Inscrições',
      'Historico de Inscrições',
      'Historico de Inscricoes',
      'Histórico de Inscricoes'
    ];

    for (const key of possibleKeys) {
      const value = response?.[key];
      if (Array.isArray(value)) return value as InscricaoDetalhadaDto[];
    }

    for (const value of Object.values(response)) {
      if (Array.isArray(value)) return value as InscricaoDetalhadaDto[];
    }

    return [];
  }

  getLeaderName(liderId: number): string {
    return this.leaderNameById[liderId] || `Usuário #${liderId}`;
  }

  private loadLeadersPublic(items: TournamentPending[]): void {
    const all = items.flatMap((i) => i.pending);
    const leaderIds = Array.from(new Set(all.map((i) => i.liderId).filter(Boolean)));

    for (const id of leaderIds) {
      if (this.leaderNameById[id]) continue;

      this.userService.buscarPublicoPorId(id).subscribe({
        next: (u) => {
          this.leaderNameById[id] = u.nickname || u.nomeDoUsuario || `Usuário #${id}`;
        },
        error: () => {
          this.leaderNameById[id] = `Usuário #${id}`;
        }
      });
    }
  }
}
