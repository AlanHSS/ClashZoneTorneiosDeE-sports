import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { InscriptionService } from '@core/services/inscription';
import { TournamentService } from '@core/services/tournament';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';
import { UserService } from '@core/services/user';
import { InscricaoDetalhadaDto, MembroEquipeDomain, StatusInscricao, TipoMembro, TorneioDomain } from '@core/models';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';

@Component({
  selector: 'app-tournament-inscriptions',
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
    StatusBadgePipe,
    StatusNamePipe
  ],
  templateUrl: './tournament-inscriptions.html',
  styleUrl: './tournament-inscriptions.scss'
})
export class TournamentInscriptionsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private inscriptionService = inject(InscriptionService);
  private tournamentService = inject(TournamentService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);
  private userService = inject(UserService);

  tournament: TorneioDomain | null = null;
  inscriptions: InscricaoDetalhadaDto[] = [];
  loading = true;
  leaderNameById: Record<number, string> = {};
  membersByTeamId: Record<number, MembroEquipeDomain[]> = {};
  membersLoadingByTeamId: Record<number, boolean> = {};
  membersOpenByTeamId: Record<number, boolean> = {};

  statusList = Object.values(StatusInscricao);

  filters = {
    status: null as StatusInscricao | null
  };

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/tournaments']);
      return;
    }

    this.loadTournament(id);
  }

  onBack(): void {
    if (this.tournament) {
      this.router.navigate(['/tournaments', this.tournament.id]);
      return;
    }
    this.router.navigate(['/tournaments']);
  }

  loadTournament(id: number): void {
    this.tournamentService.buscarPorId(id).subscribe({
      next: (data) => {
        this.tournament = data;

        if (!this.canManageFor(data)) {
          this.notificationService.error('Você não tem permissão para gerenciar inscrições deste torneio.');
          this.router.navigate(['/tournaments', id]);
          return;
        }

        this.loadInscriptions(id);
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneio');
        this.router.navigate(['/tournaments']);
      }
    });
  }

  loadInscriptions(tournamentId: number): void {
    this.loading = true;

    this.inscriptionService.listarPorTorneio(
      tournamentId,
      this.filters.status ?? undefined
    ).subscribe({
      next: (response) => {
        this.inscriptions = this.extractInscriptions(response);
        this.loadLeadersPublic(this.inscriptions);
        this.loading = false;
      },
      error: () => {
        this.inscriptions = [];
        this.loading = false;
      }
    });
  }

  onStatusFilterChange(): void {
    const id = this.tournament?.id ?? Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadInscriptions(id);
    }
  }

  toggleMembers(inscription: InscricaoDetalhadaDto): void {
    const teamId = inscription.equipeId;
    this.membersOpenByTeamId[teamId] = !this.membersOpenByTeamId[teamId];

    if (this.membersOpenByTeamId[teamId] && !this.membersByTeamId[teamId]) {
      this.loadMembers(teamId);
    }
  }

  private loadMembers(teamId: number): void {
    const tournamentId = this.tournament?.id ?? Number(this.route.snapshot.paramMap.get('id'));
    if (!tournamentId) return;

    this.membersLoadingByTeamId[teamId] = true;

    this.inscriptionService.listarMembrosEquipeNoTorneio(tournamentId, teamId).subscribe({
      next: (members) => {
        this.membersByTeamId[teamId] = members || [];
        this.membersLoadingByTeamId[teamId] = false;
      },
      error: () => {
        this.membersByTeamId[teamId] = [];
        this.membersLoadingByTeamId[teamId] = false;
      }
    });
  }

  getTitulares(teamId: number): MembroEquipeDomain[] {
    return (this.membersByTeamId[teamId] || []).filter((m) => m.tipo === TipoMembro.TITULAR);
  }

  getReservas(teamId: number): MembroEquipeDomain[] {
    return (this.membersByTeamId[teamId] || []).filter((m) => m.tipo === TipoMembro.RESERVA);
  }

  canApproveOrReject(inscription: InscricaoDetalhadaDto): boolean {
    return inscription.statusInscricao === StatusInscricao.PENDENTE;
  }

  getLeaderName(liderId: number): string {
    return this.leaderNameById[liderId] || `Usuário #${liderId}`;
  }

  private canManageFor(tournament: TorneioDomain): boolean {
    const currentUser = this.authService.getCurrentUser();
    return this.authService.isAdmin() || (!!currentUser && currentUser.id === tournament.criadorId);
  }

  private loadLeadersPublic(inscriptions: InscricaoDetalhadaDto[]): void {
    const leaderIds = Array.from(new Set(inscriptions.map((i) => i.liderId).filter(Boolean)));

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

  onApprove(inscription: InscricaoDetalhadaDto): void {
    if (!confirm(`Aprovar a inscricao da equipe "${inscription.nomeEquipe}"?`)) return;

    this.inscriptionService.atualizarStatus(inscription.id, {
      statusInscricao: StatusInscricao.APROVADA
    }).subscribe({
      next: () => {
        this.notificationService.success('Inscricao aprovada com sucesso!');
        this.onStatusFilterChange();
      }
    });
  }

  onReject(inscription: InscricaoDetalhadaDto): void {
    const motivo = prompt(`Motivo da recusa (opcional) para a equipe "${inscription.nomeEquipe}":`) || undefined;

    if (!confirm(`Recusar a inscricao da equipe "${inscription.nomeEquipe}"?`)) return;

    this.inscriptionService.atualizarStatus(inscription.id, {
      statusInscricao: StatusInscricao.RECUSADA,
      motivoRecusa: motivo
    }).subscribe({
      next: () => {
        this.notificationService.success('Inscricao recusada com sucesso!');
        this.onStatusFilterChange();
      }
    });
  }

  private extractInscriptions(response: any): InscricaoDetalhadaDto[] {
    if (!response) return [];
    if (Array.isArray(response)) return response as InscricaoDetalhadaDto[];

    const possibleKeys = [
      'Inscrições',
      'Inscricoes',
      'inscricoes',
      'inscrições'
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
}
